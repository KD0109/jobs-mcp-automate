package com.example.jobautomation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class ApplicationDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobPostingId;

    @Column(length = 8000)
    private String coverLetter;

    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String resumePath;

    private String status = "DRAFT";
    private Instant createdAt = Instant.now();
}
