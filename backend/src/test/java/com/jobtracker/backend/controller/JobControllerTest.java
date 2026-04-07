package com.jobtracker.backend.controller;

import com.jobtracker.backend.exception.JobServiceException;
import com.jobtracker.backend.model.Job;
import com.jobtracker.backend.model.JobStatus;
import com.jobtracker.backend.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
public class JobControllerTest {
    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {
        job = Job.builder()
                .jobTitle("Junior Developer")
                .company("123 Computers")
                .location("Brampton, ON")
                .appliedDate(LocalDate.of(2026, 4, 1))
                .status(JobStatus.APPLIED)
                .build();
    }

    @Nested
    @DisplayName("Get Jobs API")
    class getJobAPITests {
        @Test
        @DisplayName("GET /api/jobs - Should return all jobs")
        void shouldReturnJobs() throws Exception {
            when(jobService.getAllJobs()).thenReturn(List.of(job));

            mockmvc.perform(get("/api/jobs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].jobTitle").value("Junior Developer"));
        }

        @Test
        @DisplayName("GET /api/jobs - Should return empty list")
        void shouldReturnEmptyList() throws Exception {
            when(jobService.getAllJobs()).thenReturn(List.of());

            mockmvc.perform(get("/api/jobs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        @DisplayName("GET /api/jobs - Should return 500 when service fails")
        void shouldReturn500OnServiceError() throws Exception {
            when(jobService.getAllJobs()).thenThrow(new JobServiceException("Unexpected error occurred"));

            mockmvc.perform(get("/api/jobs"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Create Jobs API")
    class CreateJobAPITests {
        @Test
        @DisplayName("POST /api/jobs - Should create a job")
        void shouldCreateJob() throws Exception {
            when(jobService.createJob(any(Job.class))).thenReturn(job);

            mockmvc.perform(post("/api/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(job)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.jobTitle").value("Junior Developer"));

            verify(jobService).createJob(any(Job.class));
        }

        @Test
        @DisplayName("POST /api/jobs - Should return 500 when service fails")
        void shouldReturn500OnServiceError() throws Exception {
            when(jobService.createJob(any(Job.class))).thenThrow(new JobServiceException("Unexpected error occurred"));

            mockmvc.perform(post("/api/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(job)))
                    .andExpect(status().isInternalServerError());

            verify(jobService).createJob(any());
        }

        @Test
        @DisplayName("POST /api/jobs - Should return 400 when missing title")
        void shouldReturn400WhenTitleBlank() throws Exception {
            Job invalidJob = Job.builder()
                    .jobTitle("")
                    .company("123 Computers")
                    .location("Brampton, ON")
                    .appliedDate(LocalDate.of(2026, 4, 1))
                    .status(JobStatus.APPLIED)
                    .build();

            mockmvc.perform(post("/api/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidJob)))
                    .andExpect(status().isBadRequest());

            verify(jobService, never()).createJob(any());
        }
    }
}
