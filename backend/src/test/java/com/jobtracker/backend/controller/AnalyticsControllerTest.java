package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.service.AnalyticsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
public class AnalyticsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyticsService analyticsService;

    @Nested
    @DisplayName("Get Dashboard Analytics API")
    class GetDashboardAnalyticsTests {
        @Test
        @DisplayName("GET /api/analytics/dashboard - Should return analytics for dashboard")
        void shouldReturnDashboardAnalytics() throws Exception {
            Map<JobStatus, Long> statusCounts = Map.of(
                    JobStatus.APPLIED, 5L,
                    JobStatus.INTERVIEWING, 2L,
                    JobStatus.REJECTED, 3L
            );
            DashboardAnalyticsResponseDTO analytics = new DashboardAnalyticsResponseDTO(
                    10L,
                    statusCounts,
                    3L,
                    2.5,
                    5L
            );

            when(analyticsService.getDashboardAnalytics()).thenReturn(analytics);

            mockMvc.perform(get("/api/analytics/dashboard")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalApplications").value(10))
                    .andExpect(jsonPath("$.countByStatus.APPLIED").value(5))
                    .andExpect(jsonPath("$.countByStatus.REJECTED").value(3))
                    .andExpect(jsonPath("$.appliedThisWeek").value(3))
                    .andExpect(jsonPath("$.averageApplicationsPerWeek").value(2.5))
                    .andExpect(jsonPath("$.daysSinceLastApplication").value(5));
            verify(analyticsService).getDashboardAnalytics();
        }

        @Test
        @DisplayName("GET /api/analytics/dashboard - Should return zero values when no applications exist")
        void shouldReturnEmptyDashboardAnalytics() throws Exception {
            DashboardAnalyticsResponseDTO analytics = new DashboardAnalyticsResponseDTO(
                    0L,
                    Map.of(),
                    0L,
                    0.0,
                    null
            );

            when(analyticsService.getDashboardAnalytics()).thenReturn(analytics);

            mockMvc.perform(get("/api/analytics/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalApplications").value(0))
                    .andExpect(jsonPath("$.countByStatus").isEmpty())
                    .andExpect(jsonPath("$.appliedThisWeek").value(0))
                    .andExpect(jsonPath("$.averageApplicationsPerWeek").value(0.0))
                    .andExpect(jsonPath("$.daysSinceLastApplication").value(nullValue()));
            verify(analyticsService).getDashboardAnalytics();
        }
    }
}
