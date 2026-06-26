package com.example.jobautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobAutomationMcpApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobAutomationMcpApplication.class, args);
    }
}
