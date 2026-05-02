package com.resume.ai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String predictedCategory;
    
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private User candidate;
}

