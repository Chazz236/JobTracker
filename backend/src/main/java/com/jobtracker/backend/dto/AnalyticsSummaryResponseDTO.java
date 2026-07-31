package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record AnalyticsSummaryResponseDTO(
        @Schema(description = "Total number of job applications submitted", example = "1")
        Long totalApplications,

        @Schema(description = "Count of applications grouped by their status", example = "{\"APPLIED\": 25, \"INTERVIEWING\": 5, \"REJECTED\": 12}")
        Map<JobStatus, Long> countByStatus,

        @Schema(description = "Number of applications submitted during current calendar week window", example = "8")
        Long appliedThisWeek,

        @Schema(description = "Average number of applications submitted per week over the previous 8 weeks", example = "3.5")
        Double averageApplicationsPerWeek,

        @Schema(description = "Number of days since last application", example = "5")
        Long daysSinceLastApplication
) { }
