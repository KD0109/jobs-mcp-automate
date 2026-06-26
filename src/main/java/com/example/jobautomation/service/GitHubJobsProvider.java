package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GitHubJobsProvider implements JobProvider {

    @Value("${providers.github.enabled:true}")
    private boolean enabled;

    private final WebClient.Builder webClientBuilder;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String providerName() {
        return "GitHub Jobs";
    }

    @Override
    public List<JobPosting> search(String location, String country, int limit) {
        if (!isEnabled()) {
            return List.of();
        }

        try {
            List<Map<String, Object>> results = webClientBuilder.build()
                    .get()
                    .uri("https://jobs.github.com/positions.json?description=backend&location={location}", location)
                    .retrieve()
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .take(limit)
                    .collectList()
                    .blockOptional()
                    .orElse(List.of());

            List<JobPosting> postings = new ArrayList<>();
            for (Map<String, Object> item : results) {
                JobPosting posting = new JobPosting();
                posting.setSource(providerName());
                posting.setTitle((String) item.get("title"));
                posting.setCompany((String) item.get("company"));
                posting.setLocation(location);
                posting.setCountry(country);
                posting.setUrl((String) item.get("url"));
                posting.setSummary((String) item.getOrDefault("description", ""));
                posting.setPostedAt(Instant.now());
                posting.setRelevanceScore(88);
                postings.add(posting);
            }
            return postings;
        } catch (Exception ex) {
            return List.of();
        }
    }
}
