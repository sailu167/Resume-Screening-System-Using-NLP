package com.resume.ai.controller;

import com.resume.ai.service.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MLService mlService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> status = new HashMap<>();
        boolean dbStatus = false;
        boolean mlStatus = false;

        // Check DB
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                dbStatus = true;
            }
        } catch (Exception e) {
            status.put("dbError", e.getMessage());
        }

        // Check ML Service
        try {
            Map<String, Object> metrics = mlService.getMetrics();
            if (metrics != null) {
                mlStatus = true;
            }
        } catch (Exception e) {
             status.put("mlError", e.getMessage());
        }

        status.put("database", dbStatus ? "UP" : "DOWN");
        status.put("mlService", mlStatus ? "UP" : "DOWN");
        status.put("status", (dbStatus && mlStatus) ? "UP" : "DOWN");

        return ResponseEntity.ok(status);
    }
}
