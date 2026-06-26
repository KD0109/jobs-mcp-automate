package com.example.jobautomation.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProviderCatalog {
    public List<String> getPreferredProviders() {
        return List.of(
                "Adzuna",
                "Jooble",
                "Official company RSS feeds",
                "Government / university career pages"
        );
    }
}
