package com.example.jobautomation.service;

import com.example.jobautomation.model.JobPosting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobAutomationScheduler {

    private final JobDiscoveryService discoveryService;
    private final DraftService draftService;

    @Scheduled(cron = "${jobs.scheduler.cron:0 0 9,17 * * *}")
    public void runDailyDiscovery() {
        log.info("Starting scheduled job discovery run");
        List<JobPosting> jobs = discoveryService.discoverJobs();
        jobs.forEach(jobPosting -> {
            draftService.createDraft(jobPosting);
            log.info("Prepared draft for {} at {}", jobPosting.getTitle(), jobPosting.getLocation());
        });
    }
}
