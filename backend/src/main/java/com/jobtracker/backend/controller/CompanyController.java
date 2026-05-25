package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.CompanyResponseDTO;
import com.jobtracker.backend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing companies
 * Provides endpoints for reading companies via HTTP
 */
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Company Controller", description = "APIs for companies")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "Get all companies", description = "Returns a list of all companies stored in the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved companies")
    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }
}
