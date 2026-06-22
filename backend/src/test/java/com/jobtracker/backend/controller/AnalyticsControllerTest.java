package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
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
    class GetDashboardAnalyticsAPITest {
        @Test
        @DisplayName("GET /api/analytics/dashboard - Should return analytics for dashboard")
        void shouldReturnDashboardAnalytics() throws Exception {
            Map<String, Long> statusCounts = Map.of(
                    "APPLIED", 5L,
                    "INTERVIEWING", 2L,
                    "REJECTED", 3L
            );
            DashboardAnalyticsResponseDTO analytics = new DashboardAnalyticsResponseDTO(10L, statusCounts);

            when(analyticsService.getDashboardAnalytics()).thenReturn(analytics);

            mockMvc.perform(get("/api/analytics/dashboard")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalApps").value(10))
                    .andExpect(jsonPath("$.countByStatus.APPLIED").value(5))
                    .andExpect(jsonPath("$.countByStatus.REJECTED").value(3));
            verify(analyticsService).getDashboardAnalytics();
        }

        @Test
        @DisplayName("GET /api/analytics/dashboard - Should return zero values when no applications exist")
        void shouldReturnEmptyList() throws Exception {
            DashboardAnalyticsResponseDTO analytics = new DashboardAnalyticsResponseDTO(0L, Map.of());

            when(analyticsService.getDashboardAnalytics()).thenReturn(analytics);

            mockMvc.perform(get("/api/analytics/dashboard"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalApps").value(0))
                    .andExpect(jsonPath("$.countByStatus").isEmpty());
            verify(analyticsService).getDashboardAnalytics();
        }
    }
}
