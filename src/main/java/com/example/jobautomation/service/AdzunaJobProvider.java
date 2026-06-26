package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdzunaJobProvider implements JobProvider {

    @Value("${providers.adzuna.enabled:false}")
    private boolean enabled;

    @Value("${providers.adzuna.app-id:}")
    private String appId;

    @Value("${providers.adzuna.app-key:}")
    private String appKey;

    private final WebClient.Builder webClientBuilder;

    @Override
    public boolean isEnabled() {
        return enabled && !appId.isBlank() && !appKey.isBlank();
    }

    @Override
    public String providerName() {
        return "Adzuna";
    }

    @Override
    public List<JobPosting> search(String location, String country, int limit) {
        if (!isEnabled()) {
            return List.of();
        }

        Map<String, Object> response = webClientBuilder.build()
                .get()
                .uri("https://api.adzuna.com/v1/api/jobs/{country}/search/1", country.toLowerCase())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("X-Api-App-Id", appId)
                .header("X-Api-App-Key", appKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> results = (List<Map<String, Object>>) ((Map<String, Object>) response.getOrDefault("results", List.of()));
        List<JobPosting> postings = new ArrayList<>();
        for (Map<String, Object> item : results) {
            JobPosting posting = new JobPosting();
            posting.setSource(providerName());
            posting.setTitle((String) item.get("title"));
            posting.setCompany((String) ((Map<String, Object>) item.getOrDefault("company", Map.of())).getOrDefault("display_name", "Unknown"));
            posting.setLocation((String) item.getOrDefault("location", location));
            posting.setCountry(country);
            posting.setUrl((String) item.get("redirect_url"));
            posting.setSummary((String) item.getOrDefault("description", ""));
            posting.setSalary((String) ((Map<String, Object>) item.getOrDefault("salary_is_predicted", false)).getOrDefault("salary_is_predicted", ""));
            posting.setPostedAt(Instant.now());
            posting.setRelevanceScore(90);
            postings.add(posting);
            if (postings.size() >= limit) {
                break;
            }
        }
        return postings;
    }
}
