package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    @Nested
    @DisplayName("Find By Name Ignoring Case")
    class FindByNameIgnoreCaseTests {
        @Test
        @DisplayName("Should find exact company name")
        void shouldReturnExactCompany() {
            Company company = companyRepository.save(Company.builder().name("Google").build());

            Optional<Company> result = companyRepository.findByNameIgnoreCase("Google");

            assertThat(result).hasValueSatisfying(c -> {
                assertThat(c.getName()).isEqualTo("Google");
                assertThat(c.getId()).isEqualTo(company.getId());
            });
        }

        @Test
        @DisplayName("Should find company name ignoring case")
        void shouldReturnCompanyIgnoringCase() {
            Company company = companyRepository.save(Company.builder().name("Google").build());

            Optional<Company> result = companyRepository.findByNameIgnoreCase("GoOgLe");

            assertThat(result).hasValueSatisfying(c -> {
                assertThat(c.getName()).isEqualTo("Google");
                assertThat(c.getId()).isEqualTo(company.getId());
            });
        }

        @Test
        @DisplayName("Should return empty if company not found")
        void shouldReturnEmpty() {
            companyRepository.save(Company.builder().name("Google").build());

            Optional<Company> result = companyRepository.findByNameIgnoreCase("Apple");

            assertThat(result).isEmpty();
        }
    }
}
