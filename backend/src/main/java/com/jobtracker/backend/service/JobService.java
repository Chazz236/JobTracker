package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.JobRequestDTO;
import com.jobtracker.backend.dto.JobResponseDTO;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.repository.JobRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for job application business logic
 * Handles data transfer between the controller and repository
 */
@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(JobResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public JobResponseDTO createJob(JobRequestDTO request) {
        Job job = jobRepository.save(request.toEntity());
        return JobResponseDTO.fromEntity(job);
    }

    @Transactional
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job with id=" + id + " not found");
        }
        jobRepository.deleteById(id);
    }

    @Transactional
    public JobResponseDTO updateJob(Long id, JobRequestDTO request) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setJobTitle(request.jobTitle());
                    job.setCompany(request.company());
                    job.setLocation(request.location());
                    job.setAppliedDate(request.appliedDate());
                    job.setStatus(request.status());

                    Job updatedJob = jobRepository.save(job);
                    return JobResponseDTO.fromEntity(updatedJob);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Job with id=" + id + " not found"));
    }
}
