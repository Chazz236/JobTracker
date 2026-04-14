package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.JobRequestDTO;
import com.jobtracker.backend.dto.JobResponseDTO;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing job applications
 * Provides endpoints for creating, reading, updating, and deleting jobs via HTTP
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {
    private final JobService jobService;

    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequestDTO request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }
}
