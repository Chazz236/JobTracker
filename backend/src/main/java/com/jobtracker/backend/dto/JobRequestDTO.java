package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record JobRequestDTO(
    @NotBlank(message = "Job title is required")
    String jobTitle,

    @NotBlank(message = "Company is required")
    String company,

    @NotBlank(message = "Location is required")
    String location,

    @NotNull(message = "Applied date is required")
    LocalDate appliedDate,

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
