package com.resume.ai.controller;

import com.resume.ai.model.Resume;
import com.resume.ai.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        System.out.println("Resume upload request received: " + file.getOriginalFilename());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : null;
            Resume saved = resumeService.saveResume(file, username);
            System.out.println("Resume processed and saved: " + saved.getId());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            System.err.println("Resume processing failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error uploading resume: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyLatestResume() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        if (username == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }
        Resume resume = resumeService.getLatestResumeForUser(username);
        return ResponseEntity.ok(resume);
    }
}

