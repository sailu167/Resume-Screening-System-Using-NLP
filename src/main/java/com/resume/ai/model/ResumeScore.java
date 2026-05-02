package com.resume.ai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private Double score;
    
    private String matchedSkills;

    private Boolean shortlisted = false;
}

