package com.jobtracker.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record AnalyticsResponseDTO(
        @Schema(description = "Total number of job applications submitted", example = "1")
        Long totalApps,

        @Schema(description = "Count of applications grouped by their status", example = "{\"APPLIED\": 25, \"INTERVIEWING\": 5, \"REJECTED\": 12}")
        Map<String, Long> countByStatus
) {
}
