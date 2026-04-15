package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record JobRequestDTO(
        @Schema(description = "Title of job position", example = "Junior Developer")
        @NotBlank(message = "Job title is required")
        String jobTitle,

        @Schema(description = "Name of company", example = "Google")
        @NotBlank(message = "Company is required")
        String company,

        @Schema(description = "Location of job", example = "Toronto, Canada")
        @NotBlank(message = "Location is required")
        String location,

        @Schema(description = "Date application was submitted", example = "2026-04-15")
        @NotNull(message = "Applied date is required")
        LocalDate appliedDate,

        @Schema(description = "Current stage of application", example = "APPLIED")
        @NotNull(message = "Status is required")
        JobStatus status
) {
    public Job toEntity() {
        return Job.builder()
                .jobTitle(this.jobTitle())
                .company(this.company())
                .location(this.location())
                .appliedDate(this.appliedDate())
                .status(this.status())
                .build();
    }
}
