package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record JobResponseDTO(
        @Schema(description = "Unique identifier for job application", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Title of job position", example = "Junior Developer")
        String jobTitle,

        @Schema(description = "Name of company", example = "Google")
        String company,

        @Schema(description = "Location of job", example = "Toronto, Canada")
        String location,

        @Schema(description = "Date application was submitted", example = "2026-04-15")
        LocalDate appliedDate,

        @Schema(description = "Current stage of application", example = "APPLIED")
        JobStatus status
) {
    public static JobResponseDTO fromEntity(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getJobTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getAppliedDate(),
                job.getStatus()
        );
    }
}
