package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.AnalyticsSummaryResponseDTO;
import com.jobtracker.backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for analytics
 * Provides endpoints for reading analytics via HTTP
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Analytics Controller", description = "APIs for analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(summary = "Get analytics summary", description = "Returns key analytics summary")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved analytics summary")
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryResponseDTO> getAnalyticsSummary() {
        return ResponseEntity.ok(analyticsService.getAnalyticsSummary());
    }
}
