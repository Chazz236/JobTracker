package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;

import java.time.LocalDate;

public record JobResponseDTO(
        Long id,
        String jobTitle,
        String company,
        String location,
        LocalDate appliedDate,
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
