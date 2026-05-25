package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.CompanyResponseDTO;
import com.jobtracker.backend.model.Company;
import com.jobtracker.backend.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for company business logic
 * Handles data transfer between the controller and repository
 */
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(CompanyResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public Company findOrAddCompany(String name, String jobPageLink) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> Company.builder().name(name).build());

        if (jobPageLink == null || jobPageLink.isBlank()) {
            company.setJobPageLink(null);
        }
        else if (!jobPageLink.equals(company.getJobPageLink())) {
            company.setJobPageLink(jobPageLink);
        }

        return companyRepository.save(company);
    }
}
