package com.jobtracker.backend.service;

import com.jobtracker.backend.dto.CompanyResponseDTO;
import com.jobtracker.backend.model.Company;
import com.jobtracker.backend.repository.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Nested
    @DisplayName("Get Companies")
    class GetCompaniesTest {
        @Test
        @DisplayName("Should return all companies")
        void shouldReturnAllCompanies() {
            List<Company> companies = List.of(
                    Company.builder().name("Google").jobPageLink("google.ca").build(),
                    Company.builder().name("GooglyWoogly").jobPageLink("googlywoogly.ca").build()
            );

            when(companyRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(companies);

            List<CompanyResponseDTO> result = companyService.getAllCompanies();

            assertThat(result).hasSize(2);
            assertThat(result.getFirst().name()).isEqualTo("Google");
            assertThat(result.getLast().name()).isEqualTo("GooglyWoogly");
            verify(companyRepository).findAll(Sort.by(Sort.Direction.ASC, "name"));
        }

        @Test
        @DisplayName("Should return empty list if no companies in database")
        void shouldReturnEmptyListForEmpty() {
            when(companyRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(List.of());

            List<CompanyResponseDTO> result = companyService.getAllCompanies();

            assertThat(result).isEmpty();
            verify(companyRepository).findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
    }

    @Nested
    @DisplayName("Find or Add Company")
    class FindOrAddTests {
        @Test
        @DisplayName("Should create new company if it doesn't exist")
        void shouldCreateNewCompany() {
            when(companyRepository.findByNameIgnoreCase("Google")).thenReturn(Optional.empty());
            when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArgument(0));

            Company result = companyService.findOrAddCompany("Google", "google.ca");

            assertThat(result.getName()).isEqualTo("Google");
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        @DisplayName("Should update company job page link")
        void shouldUpdateCompanyLink() {
            Company company = Company.builder().name("Google").jobPageLink("google.ca").build();

            when(companyRepository.findByNameIgnoreCase("Google")).thenReturn(Optional.of(company));
            when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArgument(0));

            Company result = companyService.findOrAddCompany("Google", "googlywoogly.ca");

            assertThat(result.getName()).isEqualTo("Google");
            assertThat(result.getJobPageLink()).isEqualTo("googlywoogly.ca");
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        @DisplayName("Should not update company job page link if blank")
        void shouldNotUpdateCompanyLinkIfBlank() {
            Company company = Company.builder().name("Google").jobPageLink("google.ca").build();

            when(companyRepository.findByNameIgnoreCase("Google")).thenReturn(Optional.of(company));
            when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArgument(0));

            Company result = companyService.findOrAddCompany("Google", "");

            assertThat(result.getName()).isEqualTo("Google");
            assertThat(result.getJobPageLink()).isNull();
            verify(companyRepository).save(any(Company.class));
        }
    }
}
