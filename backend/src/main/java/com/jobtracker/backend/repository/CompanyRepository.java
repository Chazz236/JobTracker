package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface for database operations on Company entities
 * Provides built-in CRUD methods via Spring Data JPA
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByNameIgnoreCase(String name);
}
