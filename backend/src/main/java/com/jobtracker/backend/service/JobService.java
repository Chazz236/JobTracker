package com.jobtracker.backend.service;

import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.repository.JobRepository;
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

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job with id=" + id + " not found");
        }
        jobRepository.deleteById(id);
    }

    public Job updateJob(Long id, Job updatedJob) {
        return jobRepository.findById(id)
                .map(job -> {
                    job.setJobTitle(updatedJob.getJobTitle());
                    job.setCompany(updatedJob.getCompany());
                    job.setLocation(updatedJob.getLocation());
                    job.setAppliedDate(updatedJob.getAppliedDate());
                    job.setStatus(updatedJob.getStatus());
                    return jobRepository.save(job);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Job with id=" + id + " not found"));
    }
}
