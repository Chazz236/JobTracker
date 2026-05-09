package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.JobRequestDTO;
import com.jobtracker.backend.dto.JobResponseDTO;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.Company;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.repository.CompanyRepository;
import com.jobtracker.backend.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {
    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private JobService jobService;

    private Company company;
    private Job job;
    private JobRequestDTO request;

    @BeforeEach
    void setUp() {
        company = Company.builder().name("123 Computers").jobPageLink("123computers.com").build();

        job = Job.builder()
                .id(1L)
                .jobTitle("Junior Developer")
                .company(company)
                .location("Brampton, ON")
                .appliedDate(LocalDate.of(2026, 4, 1))
                .status(JobStatus.APPLIED)
                .build();

        request = new JobRequestDTO(
                "Junior Developer",
                "123 Computers",
                "123computers.com",
                "Brampton, ON",
                LocalDate.of(2026, 4, 1),
                JobStatus.APPLIED
        );
    }

    @Nested
    @DisplayName("Get Jobs")
    class GetJobTests {
        @Test
        @DisplayName("Should return all jobs")
        void shouldReturnJobs() {
            Job job2 = Job.builder()
                    .id(2L)
                    .jobTitle("Senior Developer")
                    .company(company)
                    .build();

            when(jobRepository.findAllWithCompany(any(Sort.class))).thenReturn(List.of(job2, job));

            List<JobResponseDTO> response = jobService.getAllJobs();

            assertThat(response).hasSize(2);
            assertThat(response.getLast().jobTitle()).isEqualTo("Junior Developer");
            assertThat(response.getFirst().jobTitle()).isEqualTo("Senior Developer");

            verify(jobRepository).findAllWithCompany(any(Sort.class));
        }

        @Test
        @DisplayName("Should return empty list when no jobs exist")
        void shouldReturnEmptyList() {
            when(jobRepository.findAllWithCompany(any(Sort.class))).thenReturn(List.of());

            List<JobResponseDTO> response = jobService.getAllJobs();

            assertThat(response).isEmpty();

            verify(jobRepository).findAllWithCompany(any(Sort.class));
        }
    }

    @Nested
    @DisplayName("Create Jobs")
    class CreateJobTests {
        @Test
        @DisplayName("Should create a job with an existing company")
        void shouldCreateJobWithExistingCompany() {
            when(companyRepository.findByNameIgnoreCase(request.companyName()))
                    .thenReturn(Optional.of(company));
            when(companyRepository.save(any(Company.class))).thenReturn(company);
            when(jobRepository.save(any(Job.class))).thenReturn(job);

            JobResponseDTO response = jobService.createJob(request);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.jobTitle()).isEqualTo("Junior Developer");
            assertThat(response.companyName()).isEqualTo("123 Computers");

            verify(companyRepository).findByNameIgnoreCase(request.companyName());
            verify(companyRepository).save(any(Company.class));
            verify(jobRepository).save(any(Job.class));
        }

        @Test
        @DisplayName("Should create a new company then a new job")
        void shouldCreateCompanyThenJob() {
            when(companyRepository.findByNameIgnoreCase(request.companyName()))
                    .thenReturn(Optional.empty());
            when(companyRepository.save(any(Company.class))).thenReturn(company);
            when(jobRepository.save(any(Job.class))).thenReturn(job);

            JobResponseDTO response = jobService.createJob(request);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.jobTitle()).isEqualTo("Junior Developer");
            assertThat(response.companyName()).isEqualTo("123 Computers");

            verify(companyRepository).findByNameIgnoreCase(request.companyName());
            verify(companyRepository).save(any(Company.class));
            verify(jobRepository).save(any(Job.class));
        }
    }

    @Nested
    @DisplayName("Delete Job")
    class DeleteJobTests {
        @Test
        @DisplayName("Should delete a job by id")
        void shouldDeleteJob() {
            Long id = job.getId();

            when(jobRepository.existsById(id)).thenReturn(true);

            jobService.deleteJob(id);

            verify(jobRepository).deleteById(id);
        }

        @Test
        @DisplayName("Should throw exception when deleting job that doesn't exist")
        void shouldThrowExceptionWhenIdNotFound() {
            Long id = 100L;

            when(jobRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> jobService.deleteJob(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Job with id=" + id + " not found");

            verify(jobRepository, never()).deleteById(id);
        }
    }

    @Nested
    @DisplayName("Update Job")
    class UpdateJobTests {
        @Test
        @DisplayName("Should update a job by id")
        void shouldUpdateJob() {
            Long id = job.getId();

            JobRequestDTO updateRequest = new JobRequestDTO(
                    "Senior Developer",
                    "123 Computers",
                    "123computers.com",
                    "Toronto",
                    LocalDate.of(2026, 4, 2),
                    JobStatus.INTERVIEWING
            );

            when(jobRepository.findByIdWithCompany(id)).thenReturn(Optional.of(job));
            when(companyRepository.findByNameIgnoreCase("123 Computers")).thenReturn(Optional.of(company));
            when(companyRepository.save(any(Company.class))).thenReturn(company);
            when(jobRepository.save(any(Job.class))).thenAnswer(i -> i.getArgument(0));

            JobResponseDTO response = jobService.updateJob(id, updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(id);
            assertThat(response.jobTitle()).isEqualTo("Senior Developer");
            assertThat(response.status()).isEqualTo(JobStatus.INTERVIEWING);

            verify(jobRepository).findByIdWithCompany(id);
            verify(jobRepository).save(any(Job.class));
        }

        @Test
        @DisplayName("Should throw exception when updating job that doesn't exist")
        void shouldThrowExceptionWhenIdNotFound() {
            Long id = 100L;

            when(jobRepository.findByIdWithCompany(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jobService.updateJob(id, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Job with id=" + id + " not found");

            verify(jobRepository, never()).save(any());
        }
    }
}
