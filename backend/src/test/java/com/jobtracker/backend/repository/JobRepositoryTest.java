package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Company;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("Find All Jobs With Company")
    class FindAllJobsWithCompanyTests {
        @Test
        @DisplayName("Should return all jobs with company")
        void shouldReturnJobsWithCompany() {
            Company google = companyRepository.save(Company.builder().name("Google").build());
            Company apple = companyRepository.save(Company.builder().name("Apple").build());
            Company computers = companyRepository.save(Company.builder().name("123 Computers").build());

            Job job1 = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(google)
                    .build();
            Job job2 = Job.builder()
                    .jobTitle("Junior Engineer")
                    .location("Chicago")
                    .appliedDate(LocalDate.of(2026, 5, 1))
                    .status(JobStatus.REJECTED)
                    .company(apple)
                    .build();
            Job job3 = Job.builder()
                    .jobTitle("Owner")
                    .location("Brampton")
                    .appliedDate(LocalDate.of(2026, 5, 2))
                    .status(JobStatus.ACCEPTED)
                    .company(computers)
                    .build();

            jobRepository.save(job1);
            jobRepository.save(job2);
            jobRepository.save(job3);

            entityManager.flush();
            entityManager.clear();

            List<Job> result = jobRepository.findAllWithCompany(Sort.by(Sort.Direction.DESC, "id"));

            assertThat(result).hasSize(3);
            assertThat(result.getFirst().getCompany().getName()).isEqualTo("123 Computers");
            assertThat(result.get(1).getLocation()).isEqualTo("Chicago");

            Job j = result.getLast();

            assertThat(j.getId()).isNotNull();
            assertThat(j.getStatus()).isEqualTo(JobStatus.APPLIED);
            assertThat(j.getCompany()).isNotNull();
        }

        @Test
        @DisplayName("Should return empty for empty database")
        void shouldReturnEmpty() {
            List<Job> result = jobRepository.findAllWithCompany(Sort.by(Sort.Direction.DESC, "id"));

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find Job By Id With Company")
    class FindJobByIdWithCompanyTests {
        @Test
        @DisplayName("Should return job with company id")
        void shouldReturnJobWithCompanyId() {
            Company google = companyRepository.save(Company.builder().name("Google").build());

            Job job = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(google)
                    .build();

            Long id = jobRepository.save(job).getId();

            entityManager.flush();
            entityManager.clear();

            Optional<Job> result = jobRepository.findByIdWithCompany(id);

            assertThat(result).hasValueSatisfying(j -> {
                assertThat(j.getId()).isEqualTo(id);
                assertThat(j.getCompany().getName()).isEqualTo("Google");
            });
        }

        @Test
        @DisplayName("Should return empty if job not found")
        void shouldReturnEmpty() {
            Optional<Job> result = jobRepository.findByIdWithCompany(99L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Count Jobs By Status")
    class CountJobsByStatusTests {
        @Test
        @DisplayName("Should return correct status counts grouped properly")
        void shouldReturnCorrectStatusCounts() {
            Company company = companyRepository.save(Company.builder().name("123 Computers").build());

            Job job1 = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();
            Job job2 = Job.builder()
                    .jobTitle("Junior Engineer")
                    .location("Chicago")
                    .appliedDate(LocalDate.of(2026, 5, 1))
                    .status(JobStatus.REJECTED)
                    .company(company)
                    .build();
            Job job3 = Job.builder()
                    .jobTitle("Owner")
                    .location("Brampton")
                    .appliedDate(LocalDate.of(2026, 5, 2))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();

            jobRepository.save(job1);
            jobRepository.save(job2);
            jobRepository.save(job3);

            entityManager.flush();
            entityManager.clear();

            List<JobStatusCount> result = jobRepository.countJobsByStatus();

            assertThat(result).hasSize(2);

            JobStatusCount applied = result.stream()
                    .filter(r -> r.getStatus() == JobStatus.APPLIED)
                    .findFirst()
                    .orElseThrow();

            JobStatusCount rejected = result.stream()
                    .filter(r -> r.getStatus() == JobStatus.REJECTED)
                    .findFirst()
                    .orElseThrow();

            assertThat(applied.getCount()).isEqualTo(2L);
            assertThat(rejected.getCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should return empty list when no jobs exist")
        void shouldReturnEmptyListWhenNoJobs() {
            List<JobStatusCount> results = jobRepository.countJobsByStatus();

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("Count Applications Between Dates")
    class CountApplicationsBetweenDatesTests {
        @Test
        @DisplayName("Should return number of applications between dates")
        void shouldReturnCountBetweenDates() {
            Company company = companyRepository.save(Company.builder().name("123 Computers").build());

            Job job1 = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();
            Job job2 = Job.builder()
                    .jobTitle("Junior Engineer")
                    .location("Chicago")
                    .appliedDate(LocalDate.of(2026, 5, 1))
                    .status(JobStatus.REJECTED)
                    .company(company)
                    .build();
            Job job3 = Job.builder()
                    .jobTitle("Owner")
                    .location("Brampton")
                    .appliedDate(LocalDate.of(2026, 6, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();
            Job job4 = Job.builder()
                    .jobTitle("President")
                    .location("Ottawa")
                    .appliedDate(LocalDate.of(2026, 7, 1))
                    .status(JobStatus.OFFERED)
                    .company(company)
                    .build();

            jobRepository.save(job1);
            jobRepository.save(job2);
            jobRepository.save(job3);
            jobRepository.save(job4);

            entityManager.flush();
            entityManager.clear();

            Long result = jobRepository.countApplicationsBetweenDates(
                    LocalDate.of(2026, 5, 1),
                    LocalDate.of(2026, 7, 1)
            );

            assertThat(result).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should return 0 applications when no jobs exists")
        void shouldReturnZeroWhenNoJobs() {
            Long result = jobRepository.countApplicationsBetweenDates(
                    LocalDate.of(2026, 5, 1),
                    LocalDate.of(2026, 7, 1)
            );

            assertThat(result).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should return 0 applications when no jobs between dates")
        void shouldReturnZeroWhenNoJobsBetweenDates() {
            Company company = companyRepository.save(Company.builder().name("123 Computers").build());

            Job job1 = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();
            Job job2 = Job.builder()
                    .jobTitle("Junior Engineer")
                    .location("Chicago")
                    .appliedDate(LocalDate.of(2026, 5, 1))
                    .status(JobStatus.REJECTED)
                    .company(company)
                    .build();
            Job job3 = Job.builder()
                    .jobTitle("Owner")
                    .location("Brampton")
                    .appliedDate(LocalDate.of(2026, 6, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();

            jobRepository.save(job1);
            jobRepository.save(job2);
            jobRepository.save(job3);

            entityManager.flush();
            entityManager.clear();

            Long result = jobRepository.countApplicationsBetweenDates(
                    LocalDate.of(2026, 7, 1),
                    LocalDate.of(2026, 8, 1)
            );

            assertThat(result).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("Find Most Recent Application")
    class FindMostRecentApplicationTests {
        @Test
        @DisplayName("Should return most recent application date")
        void shouldReturnMostRecentDate() {
            Company company = companyRepository.save(Company.builder().name("123 Computers").build());

            Job job1 = Job.builder()
                    .jobTitle("Junior Developer")
                    .location("Toronto")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();
            Job job2 = Job.builder()
                    .jobTitle("Junior Engineer")
                    .location("Chicago")
                    .appliedDate(LocalDate.of(2026, 5, 1))
                    .status(JobStatus.REJECTED)
                    .company(company)
                    .build();
            Job job3 = Job.builder()
                    .jobTitle("Owner")
                    .location("Brampton")
                    .appliedDate(LocalDate.of(2026, 6, 1))
                    .status(JobStatus.APPLIED)
                    .company(company)
                    .build();

            jobRepository.save(job1);
            jobRepository.save(job2);
            jobRepository.save(job3);

            entityManager.flush();
            entityManager.clear();

            Optional<LocalDate> result = jobRepository.findMostRecentApplication();

            assertThat(result).contains(LocalDate.of(2026, 6, 1));
        }

        @Test
        @DisplayName("Should return empty when no jobs exist")
        void shouldReturnEmptyWhenNoJobsExist() {
            Optional<LocalDate> result = jobRepository.findMostRecentApplication();

            assertThat(result).isEmpty();
        }
    }
}