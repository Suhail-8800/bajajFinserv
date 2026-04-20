package com.example.webhooksqlsolver.model;

import java.time.LocalDateTime;

public class ExecutionResponse {

    private Long id;
    private String regNo;
    private String sqlQuery;
    private String status;
    private LocalDateTime executedAt;
    private String userEmail;
    private String failureReason;

    public ExecutionResponse(
            Long id,
            String regNo,
            String sqlQuery,
            String status,
            LocalDateTime executedAt,
            String userEmail,
            String failureReason) {

        this.id = id;
        this.regNo = regNo;
        this.sqlQuery = sqlQuery;
        this.status = status;
        this.executedAt = executedAt;
        this.userEmail = userEmail;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFailureReason() {
        return failureReason;
    }
}