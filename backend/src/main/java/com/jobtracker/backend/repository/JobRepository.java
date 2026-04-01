package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for database operations on Job entities
 * Provides built-in CRUD methods via Spring Data JPA
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
