package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.DashboardAnalyticsResponseDTO;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.repository.JobRepository;
import com.jobtracker.backend.repository.JobStatusCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

        Map<JobStatus, Long> countByStatus = counts.stream()
                .collect(Collectors.toMap(
                        JobStatusCount::getStatus,
                        JobStatusCount::getCount
                ));

        Long totalApplications = countByStatus.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        LocalDate today = LocalDate.now();
        LocalDate startWeek = today.with(DayOfWeek.MONDAY);
        Long appliedThisWeek = jobRepository.countApplicationsBetweenDates(
                startWeek,
                today.plusDays(1));

        Long previousEightWeeksApplications = jobRepository.countApplicationsBetweenDates(
                startWeek.minusWeeks(8),
                startWeek);
        Double averageApplicationsPerWeek = previousEightWeeksApplications / 8.0;

        Long daysSinceLastApplication = jobRepository.findMostRecentApplication()
                .map(date -> ChronoUnit.DAYS.between(date, today))
                .orElse(null);

        return new DashboardAnalyticsResponseDTO(
                totalApplications,
                countByStatus,
                appliedThisWeek,
                averageApplicationsPerWeek,
                daysSinceLastApplication
        );
    }
}
