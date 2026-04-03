package com.jobtracker.backend.service;

import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {
        job = Job.builder()
                .jobTitle("Junior Developer")
                .company("123 Computers")
                .location("Brampton, ON")
                .appliedDate(LocalDate.of(2026, 4, 1))
                .status(JobStatus.APPLIED)
                .build();
    }

    @Nested
    @DisplayName("Get Jobs")
    class GetJobTests {
        @Test
        @DisplayName("Should return all jobs")
        void shouldReturnJobs() {
            Job job2 = Job.builder().jobTitle("Junior Analyst").build();

            when(jobRepository.findAll()).thenReturn(List.of(job, job2));

            List<Job> result = jobService.getAllJobs();

            assertThat(result)
                    .hasSize(2)
                    .extracting(Job::getJobTitle)
                    .containsExactly("Junior Developer", "Junior Analyst");
        }

        @Test
        @DisplayName("Should return empty list when no jobs exist")
        void shouldReturnEmptyList() {
            when(jobRepository.findAll()).thenReturn(List.of());

            List<Job> result = jobService.getAllJobs();

            assertThat(result).isEmpty();

            verify(jobRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Create Jobs")
    class CreateJobTests {
        @Test
        @DisplayName("Should create a job")
        void shouldCreateJob() {
            Job testJob = Job.builder()
                    .id(1L)
                    .jobTitle("Junior Developer")
                    .company("123 Computers")
                    .location("Brampton, ON")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .build();

            when(jobRepository.save(any(Job.class))).thenReturn(testJob);

            Job createdJob = jobService.createJob(job);

            assertThat(createdJob).isNotNull();
            assertThat(createdJob.getId()).isNotNull();
            assertThat(createdJob.getJobTitle()).isEqualTo("Junior Developer");
            assertThat(createdJob.getStatus()).isEqualTo(JobStatus.APPLIED);

            verify(jobRepository).save(job);
        }
    }

    @Nested
    @DisplayName("Delete Job")
    class DeleteJobTests {
        @Test
        @DisplayName("Should delete a job by id")
        void shouldDeleteJob() {
            Long id = 1L;

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
                    .hasMessageContaining("Job with id=" + id + " not found");

            verify(jobRepository, never()).deleteById(id);
        }
    }

    @Nested
    @DisplayName("Update Job")
    class UpdateJobTests {
        @Test
        @DisplayName("Should update a job by id")
        void shouldUpdateJob() {
            Long id = 1L;

            job.setId(id);

            Job updatedJob = Job.builder()
                    .jobTitle("Senior Developer")
                    .company("123 Computers")
                    .location("Brampton, ON")
                    .appliedDate(LocalDate.of(2026, 4, 2))
                    .status(JobStatus.INTERVIEWING)
                    .build();

            when(jobRepository.findById(id)).thenReturn(Optional.of(job));
            when(jobRepository.save(any(Job.class))).thenAnswer(i -> i.getArgument(0));

            Job result = jobService.updateJob(id, updatedJob);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getJobTitle()).isEqualTo("Senior Developer");
            assertThat(result.getStatus()).isEqualTo(JobStatus.INTERVIEWING);

            verify(jobRepository).save(job);
        }

        @Test
        @DisplayName("Should throw exception when updating job that doesn't exist")
        void shouldThrowExceptionWhenIdNotFound() {
            Long id = 100L;

            when(jobRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> jobService.updateJob(id, job))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Job with id=" + id + " not found");

            verify(jobRepository, never()).save(any());
        }
    }
}
