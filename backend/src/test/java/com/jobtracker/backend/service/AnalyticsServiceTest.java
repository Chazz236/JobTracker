package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
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

import java.util.List;

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
    class GetDashBoardAnalytics {
        @Test
        @DisplayName("Should return jobs counted by status")
        void shouldReturnJobsCountedByStatus() {
            JobStatusCount status1 = Mockito.mock(JobStatusCount.class);
            when(status1.getStatus()).thenReturn("APPLIED");
            when(status1.getCount()).thenReturn(10L);

            JobStatusCount status2 = Mockito.mock(JobStatusCount.class);
            when(status2.getStatus()).thenReturn("INTERVIEWING");
            when(status2.getCount()).thenReturn(3L);

            JobStatusCount status3 = Mockito.mock(JobStatusCount.class);
            when(status3.getStatus()).thenReturn("REJECTED");
            when(status3.getCount()).thenReturn(5L);

            when(jobRepository.countJobsByStatus()).thenReturn(List.of(status1, status2, status3));

            DashboardAnalyticsResponseDTO response = analyticsService.getDashboardAnalytics();

            assertThat(response).isNotNull();
            assertThat(response.totalApps()).isEqualTo(18L);
            assertThat(response.countByStatus())
                    .hasSize(3)
                    .containsEntry("APPLIED", 10L)
                    .containsEntry("INTERVIEWING", 3L)
                    .containsEntry("REJECTED", 5L);
            verify(jobRepository).countJobsByStatus();
        }

        @Test
        @DisplayName("Should return 0 jobs when database empty")
        void shouldReturnZeroJobs() {
            when(jobRepository.countJobsByStatus()).thenReturn(List.of());

            DashboardAnalyticsResponseDTO response = analyticsService.getDashboardAnalytics();

            assertThat(response.totalApps()).isEqualTo(0L);
            assertThat(response.countByStatus()).isEmpty();
            verify(jobRepository).countJobsByStatus();
        }
    }
}
