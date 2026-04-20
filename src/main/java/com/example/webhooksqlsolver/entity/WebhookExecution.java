// package com.example.webhooksqlsolver.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "webhook_execution")
// public class WebhookExecution {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String regNo;

//     @Column(length = 5000)
//     private String sqlQuery;

//     private String status;

//     private LocalDateTime executedAt;

//     public WebhookExecution() {
//     }

//     public WebhookExecution(String regNo, String sqlQuery, String status) {
//         this.regNo = regNo;
//         this.sqlQuery = sqlQuery;
//         this.status = status;
//         this.executedAt = LocalDateTime.now();
//     }

//     public Long getId() {
//         return id;
//     }

//     public String getRegNo() {
//         return regNo;
//     }

//     public void setRegNo(String regNo) {
//         this.regNo = regNo;
//     }

//     public String getSqlQuery() {
//         return sqlQuery;
//     }

//     public void setSqlQuery(String sqlQuery) {
//         this.sqlQuery = sqlQuery;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }

//     public LocalDateTime getExecutedAt() {
//         return executedAt;
//     }

//     public void setExecutedAt(LocalDateTime executedAt) {
//         this.executedAt = executedAt;
//     }
// }



package com.example.webhooksqlsolver.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "webhook_executions")
public class WebhookExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNo;

    @Column(columnDefinition = "TEXT")
    private String sqlQuery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status;

    private LocalDateTime executedAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private Long durationMs;

    // 🔥 NEW FIELD — Failure Reason Tracking
    @Column(columnDefinition = "TEXT")
    private String failureReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}