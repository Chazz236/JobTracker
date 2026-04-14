package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.JobRequestDTO;
import com.jobtracker.backend.dto.JobResponseDTO;
import com.jobtracker.backend.exception.JobServiceException;
import com.jobtracker.backend.exception.ResourceNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private JobResponseDTO response;
    private JobRequestDTO request;

    @BeforeEach
    void setUp() {
        response = new JobResponseDTO(
                1L,
                "Junior Developer",
                "123 Computers",
                "Brampton, ON",
                LocalDate.of(2026, 4, 1),
                JobStatus.APPLIED
        );

        request = new JobRequestDTO(
                "Junior Developer",
                "123 Computers",
                "Brampton, ON",
                LocalDate.of(2026, 4, 1),
                JobStatus.APPLIED
        );
    }

    @Nested
    @DisplayName("Get Jobs API")
    class getJobAPITests {
        @Test
        @DisplayName("GET /api/jobs - Should return all jobs")
        void shouldReturnJobs() throws Exception {
            when(jobService.getAllJobs()).thenReturn(List.of(response));

            mockmvc.perform(get("/api/jobs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].jobTitle").value("Junior Developer"))
                    .andExpect(jsonPath("$[0].id").value(1));
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
            when(jobService.createJob(any(JobRequestDTO.class))).thenReturn(response);

            mockmvc.perform(post("/api/jobs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.jobTitle").value("Junior Developer"))
                    .andExpect(jsonPath("$.id").value(1));

            verify(jobService).createJob(any(JobRequestDTO.class));
        }

        @Test
        @DisplayName("POST /api/jobs - Should return 500 when service fails")
        void shouldReturn500OnServiceError() throws Exception {
            when(jobService.createJob(any(JobRequestDTO.class))).thenThrow(new JobServiceException("Unexpected error occurred"));

            mockmvc.perform(post("/api/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError());

            verify(jobService).createJob(any());
        }

        @Test
        @DisplayName("POST /api/jobs - Should return 400 when missing title")
        void shouldReturn400WhenTitleBlank() throws Exception {
            JobRequestDTO invalidRequest = new JobRequestDTO(
                    "",
                    "123 Computers",
                    "Brampton, ON",
                    LocalDate.of(2026, 4, 1),
                    JobStatus.APPLIED
            );

            mockmvc.perform(post("/api/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(jobService, never()).createJob(any());
        }
    }

    @Nested
    @DisplayName("Delete Jobs API")
    class DeleteJobAPITests {
        @Test
        @DisplayName("DELETE /api/jobs/{id} - Should delete a job")
        void shouldDeleteJob() throws Exception {
            Long id = 1L;

            mockmvc.perform(delete("/api/jobs/{id}", id))
                    .andExpect(status().isNoContent());

            verify(jobService).deleteJob(id);
        }

        @Test
        @DisplayName("DELETE /api/jobs/{id} - Should return 500 when service fails")
        void shouldReturn500OnServiceError() throws Exception {
            Long id = 1L;

            doThrow(new JobServiceException("Unexpected error occurred"))
                    .when(jobService).deleteJob(id);

            mockmvc.perform(delete("/api/jobs/{id}", id))
                    .andExpect(status().isInternalServerError());

            verify(jobService).deleteJob(id);
        }
    }

    @Nested
    @DisplayName("Update Jobs API")
    class UpdateJobAPITests {
        @Test
        @DisplayName("PUT /api/jobs/{id} - Should update a job")
        void shouldUpdateJob() throws Exception {
            Long id = 1L;

            JobRequestDTO updatedRequest = new JobRequestDTO(
                    "Senior Developer",
                    "123 Computers",
                    "Toronto, ON",
                    LocalDate.of(2026, 4, 2),
                    JobStatus.INTERVIEWING
            );

            JobResponseDTO updatedResponse = new JobResponseDTO(
                    id,
                    "Senior Developer",
                    "123 Computers",
                    "Toronto, ON",
                    LocalDate.of(2026, 4, 2),
                    JobStatus.INTERVIEWING
            );

            when(jobService.updateJob(eq(id), any(JobRequestDTO.class))).thenReturn(updatedResponse);

            mockmvc.perform(put("/api/jobs/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.jobTitle").value("Senior Developer"));

            verify(jobService).updateJob(eq(id), any(JobRequestDTO.class));
        }

        @Test
        @DisplayName("PUT /api/jobs/{id} - Should return error status when job not found")
        void shouldReturnErrorWhenJobNotFound() throws Exception {
            Long id = 100L;

            JobRequestDTO updatedRequest = new JobRequestDTO(
                    "Senior Developer",
                    "123 Computers",
                    "Toronto, ON",
                    LocalDate.of(2026, 4, 2),
                    JobStatus.INTERVIEWING
            );

            when(jobService.updateJob(eq(id), any(JobRequestDTO.class)))
                    .thenThrow(new ResourceNotFoundException("Job with id=" + id + " not found"));

            mockmvc.perform(put("/api/jobs/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedRequest)))
                    .andExpect(status().isNotFound());

            verify(jobService).updateJob(eq(id), any(JobRequestDTO.class));
        }
    }
}
