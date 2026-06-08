package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.Job;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface for database operations on Job entities
 * Provides built-in CRUD methods via Spring Data JPA
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j JOIN FETCH j.company")
    List<Job> findAllWithCompany(Sort sort);

    @Query("SELECT j FROM Job j JOIN FETCH j.company WHERE j.id = :id")
    Optional<Job> findByIdWithCompany(Long id);

    @Query("SELECT j.status AS status, COUNT(j) AS count FROM Job j GROUP BY j.status")
    List<JobStatusCount> countJobsByStatus();
}
