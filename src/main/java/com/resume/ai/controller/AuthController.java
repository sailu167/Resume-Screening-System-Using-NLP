package com.resume.ai.controller;

import com.resume.ai.model.ERole;
import com.resume.ai.model.Role;
import com.resume.ai.model.User;
import com.resume.ai.payload.request.LoginRequest;
import com.resume.ai.payload.request.SignupRequest;
import com.resume.ai.payload.response.JwtResponse;
import com.resume.ai.payload.response.MessageResponse;
import com.resume.ai.security.jwt.JwtUtils;
import com.resume.ai.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    DataSource dataSource;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println("Registration request received for user: " + signUpRequest.getUsername());
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Database connection established for registration.");
            // Check if username exists
            String checkUser = "SELECT count(*) FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkUser)) {
                ps.setString(1, signUpRequest.getUsername());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
                    }
                }
            }

            // Check if email exists
            String checkEmail = "SELECT count(*) FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkEmail)) {
                ps.setString(1, signUpRequest.getEmail());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
                    }
                }
            }

            // Insert user
            String insertUser = "INSERT INTO users (username, email, password) VALUES (?, ?, ?) RETURNING id";
            long userId;
            try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
                ps.setString(1, signUpRequest.getUsername());
                ps.setString(2, signUpRequest.getEmail());
                ps.setString(3, encoder.encode(signUpRequest.getPassword()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getLong(1);
                    } else {
                        throw new SQLException("Failed to create user, no ID returned.");
                    }
                }
            }

            // Assign roles
            Set<String> strRoles = signUpRequest.getRole();
            if (strRoles == null || strRoles.isEmpty()) {
                assignRole(conn, userId, "ROLE_CANDIDATE");
            } else {
                for (String role : strRoles) {
                    switch (role.toLowerCase()) {
                        case "admin":
                            assignRole(conn, userId, "ROLE_ADMIN");
                            break;
                        case "recruiter":
                            assignRole(conn, userId, "ROLE_RECRUITER");
                            break;
                        default:
                            assignRole(conn, userId, "ROLE_CANDIDATE");
                    }
                }
            }

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    private void assignRole(Connection conn, long userId, String roleName) throws SQLException {
        System.out.println("Assigning role " + roleName + " to user ID " + userId);
        String findRoleId = "SELECT id FROM roles WHERE name = ?";
        int roleId;
        try (PreparedStatement ps = conn.prepareStatement(findRoleId)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    roleId = rs.getInt(1);
                } else {
                    throw new SQLException("Role " + roleName + " not found in database.");
                }
            }
        }

        String insertUserRole = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertUserRole)) {
            ps.setLong(1, userId);
            ps.setInt(2, roleId);
            ps.executeUpdate();
        }
    }
}

