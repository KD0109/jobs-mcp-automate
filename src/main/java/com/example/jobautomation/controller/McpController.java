package com.example.jobautomation.controller;

import com.example.jobautomation.model.ApplicationDraft;
import com.example.jobautomation.model.JobPosting;
import com.example.jobautomation.service.DraftService;
import com.example.jobautomation.service.JobDiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpController {

    private final JobDiscoveryService discoveryService;
    private final DraftService draftService;

    @PostMapping
    public Map<String, Object> handle(@RequestBody Map<String, Object> req) {
        String method = (String) req.get("method");

        if ("tools/list".equals(method)) {
            return response(req, Map.of("tools", List.of(
                    Map.of("name", "search_jobs", "description", "Search public jobs across broad EU and UAE locations"),
                    Map.of("name", "draft_application", "description", "Create a tailored application draft with applicant contact details")
            )));
        }

        if ("tools/call".equals(method)) {
            Map<String, Object> params = (Map<String, Object>) req.get("params");
            String name = (String) params.get("name");
            Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", Map.of());

            if ("search_jobs".equals(name)) {
                String country = (String) arguments.getOrDefault("country", "EU");
                List<JobPosting> jobs = discoveryService.discoverJobs();
                List<Map<String, Object>> payload = jobs.stream()
                        .filter(job -> country == null || country.isBlank() || job.getCountry().equalsIgnoreCase(country))
                        .map(job -> Map.<String, Object>of(
                                "title", job.getTitle(),
                                "company", job.getCompany(),
                                "location", job.getLocation(),
                                "country", job.getCountry(),
                                "summary", job.getSummary(),
                                "url", job.getUrl()
                        ))
                        .toList();
                return response(req, Map.of("jobs", payload));
            }

            if ("draft_application".equals(name)) {
                Map<String, Object> job = (Map<String, Object>) arguments.get("job");
                JobPosting placeholder = new JobPosting();
                placeholder.setTitle((String) job.getOrDefault("title", "Backend Engineer"));
                placeholder.setCompany((String) job.getOrDefault("company", "Example Company"));
                placeholder.setLocation((String) job.getOrDefault("location", "Amsterdam"));
                ApplicationDraft draft = draftService.createDraft(placeholder);
                return response(req, Map.of("draft", draft.getCoverLetter(), "draftId", draft.getId()));
            }
        }

        return response(req, Map.of("error", "Unsupported method"));
    }

    private Map<String, Object> response(Map<String, Object> req, Object result) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("jsonrpc", "2.0");
        payload.put("id", req.get("id"));
        payload.put("result", result);
        return payload;
    }
}
