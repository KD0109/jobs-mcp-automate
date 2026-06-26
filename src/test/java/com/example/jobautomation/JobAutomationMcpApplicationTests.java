package com.example.jobautomation;

import com.example.jobautomation.model.ApplicationDraft;
import com.example.jobautomation.model.JobPosting;
import com.example.jobautomation.repository.ApplicationDraftRepository;
import com.example.jobautomation.repository.JobPostingRepository;
import com.example.jobautomation.service.DraftService;
import com.example.jobautomation.service.JobDiscoveryService;
import com.example.jobautomation.service.JobProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JobAutomationMcpApplicationTests {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private ApplicationDraftRepository applicationDraftRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void discoverJobsShouldCreateBroadEuLocationsAndRespectLimits() {
        JobProvider provider = new JobProvider() {
            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public String providerName() {
                return "Test Provider";
            }

            @Override
            public List<JobPosting> search(String location, String country, int limit) {
                JobPosting posting = new JobPosting();
                posting.setSource(providerName());
                posting.setTitle("Backend Engineer");
                posting.setCompany("Example");
                posting.setLocation(location);
                posting.setCountry(country);
                posting.setUrl("https://example.com");
                posting.setSummary("Test posting");
                return List.of(posting);
            }
        };

        JobDiscoveryService service = new JobDiscoveryService(jobPostingRepository, List.of(provider));
        ReflectionTestUtils.setField(service, "countries", "EU");
        ReflectionTestUtils.setField(service, "locations", "Amsterdam,Berlin,Paris");
        ReflectionTestUtils.setField(service, "limitPerProvider", 3);
        ReflectionTestUtils.setField(service, "maxApiCalls", 3);
        when(jobPostingRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<JobPosting> jobs = service.discoverJobs();

        assertThat(jobs).hasSize(3);
        assertThat(jobs).extracting(JobPosting::getCountry).containsOnly("EU");
        assertThat(jobs).extracting(JobPosting::getLocation).containsExactly("Amsterdam", "Berlin", "Paris");
    }

    @Test
    void draftServiceShouldIncludeApplicantDetailsAndResumePaths() {
        DraftService service = new DraftService(applicationDraftRepository);
        ReflectionTestUtils.setField(service, "applicantName", "Kaustav Datta");
        ReflectionTestUtils.setField(service, "applicantEmail", "k.datta191994@gmail.com");
        ReflectionTestUtils.setField(service, "applicantPhone", "+917980613041");
        ReflectionTestUtils.setField(service, "resumeEu", "C:/EU/resume.pdf");
        ReflectionTestUtils.setField(service, "resumeUae", "C:/UAE/resume.pdf");
        when(applicationDraftRepository.save(any(ApplicationDraft.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobPosting jobPosting = new JobPosting();
        jobPosting.setCountry("EU");
        jobPosting.setLocation("Berlin");
        jobPosting.setCompany("Example EU Tech");

        ApplicationDraft draft = service.createDraft(jobPosting);

        assertThat(draft.getCoverLetter())
                .contains("k.datta191994@gmail.com")
                .contains("+917980613041")
                .contains("C:/EU/resume.pdf")
                .contains("Berlin");
        assertThat(draft.getApplicantEmail()).isEqualTo("k.datta191994@gmail.com");
        assertThat(draft.getApplicantPhone()).isEqualTo("+917980613041");
        verify(applicationDraftRepository).save(any(ApplicationDraft.class));
    }
}
