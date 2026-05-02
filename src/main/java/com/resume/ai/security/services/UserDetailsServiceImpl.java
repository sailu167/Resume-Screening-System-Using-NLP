package com.resume.ai.security.services;

import com.resume.ai.model.ERole;
import com.resume.ai.model.Role;
import com.resume.ai.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    DataSource dataSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try (Connection conn = dataSource.getConnection()) {
            // Pure JDBC query to find user
            String userQuery = "SELECT id, username, email, password FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(userQuery)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        
                        // Load roles using pure JDBC
                        Set<Role> roles = new HashSet<>();
                        String rolesQuery = "SELECT r.id, r.name FROM roles r " +
                                           "JOIN user_roles ur ON r.id = ur.role_id " +
                                           "WHERE ur.user_id = ?";
                        try (PreparedStatement psRoles = conn.prepareStatement(rolesQuery)) {
                            psRoles.setLong(1, user.getId());
                            try (ResultSet rsRoles = psRoles.executeQuery()) {
                                while (rsRoles.next()) {
                                    Role role = new Role();
                                    role.setId(rsRoles.getInt("id"));
                                    role.setName(ERole.valueOf(rsRoles.getString("name")));
                                    roles.add(role);
                                }
                            }
                        }
                        user.setRoles(roles);
                        return UserDetailsImpl.build(user);
                    } else {
                        throw new UsernameNotFoundException("User Not Found with username: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during authentication", e);
        }
    }
}

