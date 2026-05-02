package com.resume.ai.controller;

import com.resume.ai.model.Job;
import com.resume.ai.model.ResumeScore;
import com.resume.ai.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        System.out.println("Creating new job: " + job.getTitle());
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @PostMapping("/{jobId}/rank")
    public ResponseEntity<String> rankResumes(@PathVariable Long jobId) {
        System.out.println("Ranking resumes for job ID: " + jobId);
        try {
            jobService.rankResumesForJob(jobId);
            return ResponseEntity.ok("Ranking complete");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Ranking failed: " + e.getMessage());
        }
    }

    @PostMapping("/{jobId}/shortlist/{resumeId}")
    public ResponseEntity<String> shortlistResume(
            @PathVariable Long jobId,
            @PathVariable Long resumeId,
            @RequestParam(defaultValue = "true") boolean shortlisted
    ) {
        try {
            jobService.updateShortlist(jobId, resumeId, shortlisted);
            return ResponseEntity.ok("Shortlist updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Shortlist update failed: " + e.getMessage());
        }
    }

    @GetMapping("/{jobId}/ranks")
    public ResponseEntity<List<ResumeScore>> getRankedResumes(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getRankedResumes(jobId));
    }
}

