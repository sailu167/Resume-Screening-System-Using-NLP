package com.resume.ai.service;

import com.resume.ai.model.Job;
import com.resume.ai.model.Resume;
import com.resume.ai.model.ResumeScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobService {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private MLService mlService;

    public Job createJob(Job job) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO jobs (title, description, category, recruiter_id, created_at) VALUES (?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, job.getTitle());
                ps.setString(2, job.getDescription());
                ps.setString(3, job.getCategory());
                if (job.getRecruiter() != null) {
                    ps.setLong(4, job.getRecruiter().getId());
                } else {
                    ps.setNull(4, Types.BIGINT);
                }
                ps.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        job.setId(rs.getLong(1));
                        return job;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating job", e);
        }
        return null;
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, title, description, category, recruiter_id, created_at FROM jobs";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job job = new Job();
                    job.setId(rs.getLong("id"));
                    job.setTitle(rs.getString("title"));
                    job.setDescription(rs.getString("description"));
                    job.setCategory(rs.getString("category"));
                    job.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    jobs.add(job);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching jobs", e);
        }
        return jobs;
    }

    public void rankResumesForJob(Long jobId) {
        Job job = getJobById(jobId);
        List<Resume> resumes = resumeService.getAllResumes();

        try (Connection conn = dataSource.getConnection()) {
            // Clear existing scores for this job
            String deleteSql = "DELETE FROM resume_scores WHERE job_id = ?";
            try (PreparedStatement dps = conn.prepareStatement(deleteSql)) {
                dps.setLong(1, jobId);
                dps.executeUpdate();
            }

            for (Resume resume : resumes) {
                // ResumeService only stores placeholder for content now, but for ranking we need actual content
                // In a real app, you'd fetch it. For now let's assume content is in the Resume object
                Map<String, Object> result = mlService.calculateScore(resume.getContent(), job.getDescription());
                
                String insertSql = "INSERT INTO resume_scores (job_id, resume_id, score, matched_skills, shortlisted) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ips = conn.prepareStatement(insertSql)) {
                    ips.setLong(1, jobId);
                    ips.setLong(2, resume.getId());
                    ips.setDouble(3, ((Number) result.get("score")).doubleValue());
                    List<String> skills = (List<String>) result.get("matched_skills");
                    ips.setString(4, String.join(", ", skills));
                    ips.setBoolean(5, false);
                    ips.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error ranking resumes", e);
        }
    }

    private Job getJobById(Long jobId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, title, description, category FROM jobs WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, jobId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Job job = new Job();
                        job.setId(rs.getLong("id"));
                        job.setTitle(rs.getString("title"));
                        job.setDescription(rs.getString("description"));
                        job.setCategory(rs.getString("category"));
                        return job;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching job", e);
        }
        throw new RuntimeException("Job not found");
    }

    public List<ResumeScore> getRankedResumes(Long jobId) {
        List<ResumeScore> scores = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT rs.id, rs.job_id, rs.resume_id, rs.score, rs.matched_skills, rs.shortlisted, r.file_name " +
                         "FROM resume_scores rs " +
                         "JOIN resumes r ON rs.resume_id = r.id " +
                         "WHERE rs.job_id = ? ORDER BY rs.score DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, jobId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ResumeScore score = new ResumeScore();
                        score.setId(rs.getLong("id"));
                        score.setScore(rs.getDouble("score"));
                        score.setMatchedSkills(rs.getString("matched_skills"));
                        score.setShortlisted(rs.getBoolean("shortlisted"));
                        
                        Resume resume = new Resume();
                        resume.setId(rs.getLong("resume_id"));
                        resume.setFileName(rs.getString("file_name"));
                        score.setResume(resume);
                        
                        scores.add(score);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ranked resumes", e);
        }
        return scores;
    }

    public void updateShortlist(Long jobId, Long resumeId, boolean shortlisted) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE resume_scores SET shortlisted = ? WHERE job_id = ? AND resume_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, shortlisted);
                ps.setLong(2, jobId);
                ps.setLong(3, resumeId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating shortlist", e);
        }
    }
}

