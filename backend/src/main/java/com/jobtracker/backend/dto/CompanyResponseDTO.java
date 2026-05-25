package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.Company;
import io.swagger.v3.oas.annotations.media.Schema;

public record CompanyResponseDTO(
        @Schema(description = "Unique identifier for company", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Name of company", example = "Google")
        String name,

        @Schema(description = "Link to the company's career page", example = "https://google.com/careers")
        String jobPageLink
) {
    public static CompanyResponseDTO fromEntity(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getJobPageLink()
        );
    }
}
