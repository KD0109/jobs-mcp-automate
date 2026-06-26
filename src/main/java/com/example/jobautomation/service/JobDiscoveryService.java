package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;
import com.example.jobautomation.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobDiscoveryService {

    private final JobPostingRepository repository;
    private final List<JobProvider> providers;

    @Value("${jobs.scheduler.countries:EU,UAE}")
    private String countries;

    @Value("${jobs.scheduler.locations:Amsterdam,Berlin,Paris,Barcelona,Milan,Stockholm,Zurich,London,Dublin,Oslo,Brussels,Helsinki,Prague,Vienna,Warsaw}")
    private String locations;

    @Value("${jobs.scheduler.limit-per-provider:10}")
    private int limitPerProvider;

    @Value("${jobs.scheduler.max-api-calls:20}")
    private int maxApiCalls;

    public List<JobPosting> discoverJobs() {
        List<JobPosting> discovered = new ArrayList<>();
        List<String> locationList = Arrays.stream(locations.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        List<String> countryList = Arrays.stream(countries.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        int target = Math.max(limitPerProvider, maxApiCalls);
        for (String country : countryList) {
            for (String location : locationList) {
                if (discovered.size() >= target) {
                    break;
                }

                for (JobProvider provider : providers) {
                    if (!provider.isEnabled()) {
                        continue;
                    }

                    List<JobPosting> providerResults = provider.search(location, country, Math.max(1, target - discovered.size()));
                    if (providerResults != null && !providerResults.isEmpty()) {
                        discovered.addAll(providerResults);
                    }

                    if (discovered.size() >= target) {
                        break;
                    }
                }
            }
        }

        if (discovered.size() > target) {
            discovered = discovered.subList(0, target);
        }

        return repository.saveAll(discovered);
    }
}
