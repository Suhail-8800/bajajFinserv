package com.example.webhooksqlsolver.model;

public class ExecutionSummaryResponse {

    private long totalExecutions;
    private long successCount;
    private long failureCount;
    private double successRate;
    private double averageDurationMs;

    public ExecutionSummaryResponse(
            long totalExecutions,
            long successCount,
            long failureCount,
            double successRate,
            double averageDurationMs) {

        this.totalExecutions = totalExecutions;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.successRate = successRate;
        this.averageDurationMs = averageDurationMs;
    }

    public long getTotalExecutions() {
        return totalExecutions;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public double getAverageDurationMs() {
        return averageDurationMs;
    }
}
