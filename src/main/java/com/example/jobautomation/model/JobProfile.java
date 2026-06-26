package com.example.jobautomation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "Backend Engineer";
    private String summary = "9 years of experience in Java, Spring Boot, REST APIs, microservices, PostgreSQL";
    private String targetCountries = "EU,UAE";
    private String preferredLocations = "Amsterdam, Rotterdam, Brussels, Dubai, Abu Dhabi";
    private String skills = "Java, Spring Boot, REST, Microservices, PostgreSQL, Docker, AWS";
    private Integer yearsOfExperience = 9;
}
