package com.resume.ai.repository;

import com.resume.ai.model.ResumeScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResumeScoreRepository extends JpaRepository<ResumeScore, Long> {
    List<ResumeScore> findByJobIdOrderByScoreDesc(Long jobId);
}

