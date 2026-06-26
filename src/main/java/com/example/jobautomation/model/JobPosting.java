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
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String title;
    private String company;
    private String location;
    private String country;
    private String url;

    @Column(length = 4000)
    private String summary;

    private String salary;
    private Instant postedAt;
    private Instant discoveredAt = Instant.now();
    private Integer relevanceScore;
    private String applicationStatus = "NEW";
}
