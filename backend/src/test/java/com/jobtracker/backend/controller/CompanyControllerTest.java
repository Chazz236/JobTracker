package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.CompanyResponseDTO;
import com.jobtracker.backend.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @Nested
    @DisplayName("Get Companies API")
    class GetCompanyAPITest {
        @Test
        @DisplayName("GET /api/companies - Should return companies")
        void shouldReturnCompanies() throws Exception {
            List<CompanyResponseDTO> companies = List.of(
                    new CompanyResponseDTO(1L, "Google", "google.ca"),
                    new CompanyResponseDTO(1L, "GooglyWoogly", "googlywoogly.ca")
            );

            when(companyService.getAllCompanies()).thenReturn(companies);

            mockMvc.perform(get("/api/companies")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].name").value("Google"))
                    .andExpect(jsonPath("$[1].name").value("GooglyWoogly"));
            verify(companyService).getAllCompanies();
        }

        @Test
        @DisplayName("GET /api/companies - Should return empty list with no matches")
        void shouldReturnEmptyList() throws Exception {
            when(companyService.getAllCompanies()).thenReturn(List.of());

            mockMvc.perform(get("/api/companies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(companyService).getAllCompanies();
        }
    }
}
