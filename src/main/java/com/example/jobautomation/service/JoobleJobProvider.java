package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JoobleJobProvider implements JobProvider {

    @Value("${providers.jooble.enabled:false}")
    private boolean enabled;

    @Value("${providers.jooble.api-key:}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;

    @Override
    public boolean isEnabled() {
        return enabled && !apiKey.isBlank();
    }

    @Override
    public String providerName() {
        return "Jooble";
    }

    @Override
    public List<JobPosting> search(String location, String country, int limit) {
        if (!isEnabled()) {
            return List.of();
        }

        Map<String, Object> body = Map.of(
                "keywords", "backend engineer",
                "location", location + ", " + country,
                "page", 1
        );

        Map<String, Object> response = webClientBuilder.build()
                .post()
                .uri("https://jooble.org/api/" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getOrDefault("jobs", List.of());
        List<JobPosting> postings = new ArrayList<>();
        for (Map<String, Object> item : results) {
            JobPosting posting = new JobPosting();
            posting.setSource(providerName());
            posting.setTitle((String) item.get("title"));
            posting.setCompany((String) item.get("company"));
            posting.setLocation((String) item.getOrDefault("location", location));
            posting.setCountry(country);
            posting.setUrl((String) item.get("link"));
            posting.setSummary((String) item.getOrDefault("snippet", ""));
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
