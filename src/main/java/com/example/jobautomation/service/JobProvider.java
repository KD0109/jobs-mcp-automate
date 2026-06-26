package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;

import java.util.List;

public interface JobProvider {
    boolean isEnabled();

    String providerName();

    List<JobPosting> search(String location, String country, int limit);
}
