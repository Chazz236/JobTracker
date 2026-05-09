package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.JobRequestDTO;
import com.jobtracker.backend.dto.JobResponseDTO;
import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.Company;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.repository.CompanyRepository;
import com.jobtracker.backend.repository.JobRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final CompanyRepository companyRepository;

    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAllWithCompany(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(JobResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public JobResponseDTO createJob(JobRequestDTO request) {
        Company company = findOrAddCompany(request.companyName(), request.companyJobPageLink());
        Job job = Job.builder()
                .jobTitle(request.jobTitle())
                .company(company)
                .location(request.location())
                .appliedDate(request.appliedDate())
                .status(request.status())
                .build();
        return JobResponseDTO.fromEntity(jobRepository.save(job));
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
        return jobRepository.findByIdWithCompany(id)
                .map(job -> {
                    Company company = findOrAddCompany(request.companyName(), request.companyJobPageLink());

                    job.setJobTitle(request.jobTitle());
                    job.setCompany(company);
                    job.setLocation(request.location());
                    job.setAppliedDate(request.appliedDate());
                    job.setStatus(request.status());

                    return JobResponseDTO.fromEntity(jobRepository.save(job));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Job with id=" + id + " not found"));
    }

    private Company findOrAddCompany(String name, String jobPageLink) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> Company.builder().name(name).build());

        if (jobPageLink != null && !jobPageLink.isBlank() && !jobPageLink.equals(company.getJobPageLink())) {
            company.setJobPageLink(jobPageLink);
        }

        return companyRepository.save(company);
    }
}
