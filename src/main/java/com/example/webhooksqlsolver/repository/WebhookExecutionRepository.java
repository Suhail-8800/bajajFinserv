package com.example.webhooksqlsolver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.webhooksqlsolver.entity.ExecutionStatus;
import com.example.webhooksqlsolver.entity.User;
import com.example.webhooksqlsolver.entity.WebhookExecution;

@Repository
public interface WebhookExecutionRepository
        extends JpaRepository<WebhookExecution, Long> {

    // =========================
    // Pagination (Existing)
    // =========================
    Page<WebhookExecution> findByUser(User user, Pageable pageable);

    // =========================
    // Analytics Methods
    // =========================

    long countByUser(User user);

    long countByUserAndStatus(User user, ExecutionStatus status);

    @Query("""
            SELECT AVG(w.durationMs)
            FROM WebhookExecution w
            WHERE w.user = :user
            AND w.status = com.example.webhooksqlsolver.entity.ExecutionStatus.SUCCESS
           """)
    Double findAverageDurationByUser(@Param("user") User user);
}
