package com.resume.ai.service;

import com.resume.ai.model.Resume;
import com.resume.ai.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private MLService mlService;

    @Autowired
    private DataSource dataSource;

    public Resume saveResume(MultipartFile file, String username) {
        String fileName = file.getOriginalFilename();
        String predictedCategory = "Unknown";
        String extractedText = "";
        Long candidateId = null;
        
        // Call ML service to get category and extracted text
        try {
            Map<String, Object> prediction = mlService.predictCategory(file);
            if (prediction != null) {
                if (prediction.containsKey("category")) {
                    predictedCategory = (String) prediction.get("category");
                }
                if (prediction.containsKey("extracted_text")) {
                    extractedText = (String) prediction.get("extracted_text");
                }
            }
        } catch (Exception e) {
            System.err.println("Error calling ML service: " + e.getMessage());
        }

        // Resolve candidate ID (if available)
        if (username != null) {
            candidateId = findUserIdByUsername(username);
        }

        // Pure JDBC to save resume
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO resumes (file_name, predicted_category, content, candidate_id) VALUES (?, ?, ?, ?) RETURNING id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fileName);
                ps.setString(2, predictedCategory);
                ps.setString(3, extractedText);
                if (candidateId != null) {
                    ps.setLong(4, candidateId);
                } else {
                    ps.setNull(4, Types.BIGINT);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Resume resume = new Resume();
                        resume.setId(rs.getLong(1));
                        resume.setFileName(fileName);
                        resume.setPredictedCategory(predictedCategory);
                        resume.setContent(extractedText);
                        return resume;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error saving resume", e);
        }
        return null;
    }

    public List<Resume> getAllResumes() {
        List<Resume> resumes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, file_name, predicted_category, content FROM resumes";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Resume resume = new Resume();
                    resume.setId(rs.getLong("id"));
                    resume.setFileName(rs.getString("file_name"));
                    resume.setPredictedCategory(rs.getString("predicted_category"));
                    resume.setContent(rs.getString("content"));
                    resumes.add(resume);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error fetching resumes", e);
        }
        return resumes;
    }

    public Resume getLatestResumeForUser(String username) {
        Long userId = findUserIdByUsername(username);
        if (userId == null) {
            return null;
        }
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, file_name, predicted_category, content FROM resumes WHERE candidate_id = ? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Resume resume = new Resume();
                        resume.setId(rs.getLong("id"));
                        resume.setFileName(rs.getString("file_name"));
                        resume.setPredictedCategory(rs.getString("predicted_category"));
                        resume.setContent(rs.getString("content"));
                        return resume;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error fetching latest resume", e);
        }
        return null;
    }

    private Long findUserIdByUsername(String username) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error resolving user", e);
        }
        return null;
    }
}

