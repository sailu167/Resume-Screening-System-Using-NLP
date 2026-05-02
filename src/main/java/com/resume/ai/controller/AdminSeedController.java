package com.resume.ai.controller;

import com.resume.ai.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminSeedController {

    @Autowired
    private SeedService seedService;

    @PostMapping("/seed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> seedData(
            @RequestParam(defaultValue = "10") int jobs,
            @RequestParam(defaultValue = "100") int resumes
    ) {
        int jobsInserted = seedService.seedJobs(jobs);
        int resumesInserted = seedService.seedResumesFromCsv(resumes);

        Map<String, Object> result = new HashMap<>();
        result.put("jobsInserted", jobsInserted);
        result.put("resumesInserted", resumesInserted);
        return ResponseEntity.ok(result);
    }
}

