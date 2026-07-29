package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.repository.JobStatusCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {
    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Nested
    @DisplayName("Get Dashboard Analytics")
    class GetDashboardAnalyticsTests {
        @Test
        @DisplayName("Should return dashboard analytics")
        void shouldReturnDashboardAnalytics() {
            JobStatusCount appliedCount = Mockito.mock(JobStatusCount.class);
            JobStatusCount interviewingCount = Mockito.mock(JobStatusCount.class);
            JobStatusCount rejectedCount = Mockito.mock(JobStatusCount.class);
            LocalDate today = LocalDate.now();
            LocalDate startWeek = today.with(DayOfWeek.MONDAY);
            LocalDate eightWeeksAgo = startWeek.minusWeeks(8);

            when(appliedCount.getStatus()).thenReturn(JobStatus.APPLIED);
            when(appliedCount.getCount()).thenReturn(10L);

            when(interviewingCount.getStatus()).thenReturn(JobStatus.INTERVIEWING);
            when(interviewingCount.getCount()).thenReturn(3L);

            when(rejectedCount.getStatus()).thenReturn(JobStatus.REJECTED);
            when(rejectedCount.getCount()).thenReturn(5L);

            when(jobRepository.countJobsByStatus()).thenReturn(List.of(appliedCount, interviewingCount, rejectedCount));
            when(jobRepository.countApplicationsBetweenDates(startWeek, today.plusDays(1))).thenReturn(3L);
            when(jobRepository.countApplicationsBetweenDates(eightWeeksAgo, startWeek)).thenReturn(24L);
            when(jobRepository.findMostRecentApplication()).thenReturn(Optional.of(today.minusDays(5)));

            DashboardAnalyticsResponseDTO response = analyticsService.getDashboardAnalytics();

            assertThat(response).isNotNull();
            assertThat(response.totalApplications()).isEqualTo(18L);
            assertThat(response.countByStatus())
                    .hasSize(3)
                    .containsEntry(JobStatus.APPLIED, 10L)
                    .containsEntry(JobStatus.INTERVIEWING, 3L)
                    .containsEntry(JobStatus.REJECTED, 5L);
            assertThat(response.appliedThisWeek()).isEqualTo(3L);
            assertThat(response.averageApplicationsPerWeek()).isEqualTo(3.0);
            assertThat(response.daysSinceLastApplication()).isEqualTo(5L);
            verify(jobRepository).countJobsByStatus();
            verify(jobRepository).countApplicationsBetweenDates(startWeek, today.plusDays(1));
            verify(jobRepository).countApplicationsBetweenDates(eightWeeksAgo, startWeek);
            verify(jobRepository).findMostRecentApplication();
        }

        @Test
        @DisplayName("Should return empty dashboard analytics")
        void shouldReturnEmptyDashboardAnalytics() {
            LocalDate today = LocalDate.now();
            LocalDate startWeek = today.with(DayOfWeek.MONDAY);
            LocalDate eightWeeksAgo = startWeek.minusWeeks(8);

            when(jobRepository.countJobsByStatus()).thenReturn(List.of());
            when(jobRepository.countApplicationsBetweenDates(startWeek, today.plusDays(1))).thenReturn(0L);
            when(jobRepository.countApplicationsBetweenDates(eightWeeksAgo, startWeek)).thenReturn(0L);
            when(jobRepository.findMostRecentApplication()).thenReturn(Optional.empty());

            DashboardAnalyticsResponseDTO response = analyticsService.getDashboardAnalytics();

            assertThat(response.totalApplications()).isEqualTo(0L);
            assertThat(response.countByStatus()).isEmpty();
            assertThat(response.appliedThisWeek()).isEqualTo(0L);
            assertThat(response.averageApplicationsPerWeek()).isEqualTo(0.0);
            assertThat(response.daysSinceLastApplication()).isNull();
            verify(jobRepository).countJobsByStatus();
            verify(jobRepository).countApplicationsBetweenDates(startWeek, today.plusDays(1));
            verify(jobRepository).countApplicationsBetweenDates(eightWeeksAgo, startWeek);
            verify(jobRepository).findMostRecentApplication();
        }
    }
}
