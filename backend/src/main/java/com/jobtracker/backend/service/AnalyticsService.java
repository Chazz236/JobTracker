package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.repository.JobStatusCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for analytics business logic
 * Handles data transfer between the analytics controller and job repository
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final JobRepository jobRepository;

    public DashboardAnalyticsResponseDTO getDashboardAnalytics() {
        List<JobStatusCount> counts = jobRepository.countJobsByStatus();
        return DashboardAnalyticsResponseDTO.fromProjection(counts);
    }
}
