package com.jobtracker.backend.dto;

import com.jobtracker.backend.repository.JobStatusCount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AnalyticsResponseDTO(
        @Schema(description = "Total number of job applications submitted", example = "1")
        Long totalApps,

        @Schema(description = "Count of applications grouped by their status", example = "{\"APPLIED\": 25, \"INTERVIEWING\": 5, \"REJECTED\": 12}")
        Map<String, Long> countByStatus
) {
        public static AnalyticsResponseDTO fromProjection(List<JobStatusCount> projection) {
                Map<String, Long> statuses = projection.stream()
                        .collect(Collectors.toMap(
                                JobStatusCount::getStatus,
                                JobStatusCount::getCount
                        ));

                Long total = statuses.values().stream()
                        .mapToLong(Long::longValue)
                        .sum();

                return new AnalyticsResponseDTO(
                        total,
                        statuses
                );
        }
}
