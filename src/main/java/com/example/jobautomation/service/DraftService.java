package com.example.jobautomation.service;

import com.example.jobautomation.model.ApplicationDraft;
import com.example.jobautomation.model.JobPosting;
import com.example.jobautomation.repository.ApplicationDraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DraftService {

    private final ApplicationDraftRepository repository;

    @Value("${applicant.name:Kaustav Datta}")
    private String applicantName;

    @Value("${applicant.email:k.datta191994@gmail.com}")
    private String applicantEmail;

    @Value("${applicant.phone:+917980613041}")
    private String applicantPhone;

    @Value("${applicant.resume-eu:C:/Users/kaustav.a.datta/Downloads/Kaustav_Datta_Resume_2026.pdf}")
    private String resumeEu;

    @Value("${applicant.resume-uae:C:/Users/kaustav.a.datta/Downloads/Kaustav_Datta_FullStack_2026.pdf}")
    private String resumeUae;

    public ApplicationDraft createDraft(JobPosting jobPosting) {
        ApplicationDraft draft = new ApplicationDraft();
        draft.setJobPostingId(jobPosting.getId());
        String resumePath = "EU".equalsIgnoreCase(jobPosting.getCountry()) ? resumeEu : resumeUae;
        String coverLetter = "Hi " + jobPosting.getCompany() + " team,\n\n" +
                "I am a backend engineer with 9 years of experience in Java, Spring Boot, REST APIs, microservices and PostgreSQL. " +
                "I would be excited to contribute to your team in " + jobPosting.getLocation() + " and help deliver reliable services.\n\n" +
                "Contact: " + applicantEmail + " | " + applicantPhone + "\n" +
                "Resume: " + resumePath + "\n\n" +
                "Best regards,\n" + applicantName;
        draft.setCoverLetter(coverLetter);
        draft.setApplicantName(applicantName);
        draft.setApplicantEmail(applicantEmail);
        draft.setApplicantPhone(applicantPhone);
        draft.setResumePath(resumePath);
        return repository.save(draft);
    }
}
