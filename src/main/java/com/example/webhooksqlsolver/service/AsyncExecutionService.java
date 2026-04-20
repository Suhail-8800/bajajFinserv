package com.example.webhooksqlsolver.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import com.example.webhooksqlsolver.entity.ExecutionStatus;
import com.example.webhooksqlsolver.entity.WebhookExecution;
import com.example.webhooksqlsolver.model.SolutionRequest;
import com.example.webhooksqlsolver.model.WebhookRequest;
import com.example.webhooksqlsolver.model.WebhookResponse;
import com.example.webhooksqlsolver.repository.WebhookExecutionRepository;

@Service
public class AsyncExecutionService {

    private static final Logger logger =
            LoggerFactory.getLogger(AsyncExecutionService.class);

    private final RestTemplate restTemplate;
    private final SqlProblemSolver sqlProblemSolver;
    private final WebhookExecutionRepository executionRepository;

    @Value("${webhook.generate.url}")
    private String webhookGenerateUrl;

    @Value("${app.user.name}")
    private String userName;

    @Value("${app.user.regNo}")
    private String regNo;

    @Value("${app.user.email}")
    private String userEmail;

    public AsyncExecutionService(
            RestTemplate restTemplate,
            SqlProblemSolver sqlProblemSolver,
            WebhookExecutionRepository executionRepository) {

        this.restTemplate = restTemplate;
        this.sqlProblemSolver = sqlProblemSolver;
        this.executionRepository = executionRepository;
    }

    @Async("webhookExecutor")
    public void processWebhookAsync(Long executionId) {

        WebhookExecution execution =
                executionRepository.findById(executionId)
                        .orElseThrow();

        try {

            execution.setStatus(ExecutionStatus.RUNNING);
            execution.setStartedAt(LocalDateTime.now());
            executionRepository.save(execution);

            // 1️⃣ Generate webhook
            WebhookResponse webhookResponse =
                    generateWebhookWithResilience();

            // 2️⃣ Solve SQL
            String sqlSolution =
                    sqlProblemSolver.solveProblem(regNo);

            // 3️⃣ Submit solution (resilient + timeout)
            submitSolutionWithResilience(
                    webhookResponse.getWebhook(),
                    webhookResponse.getAccessToken(),
                    sqlSolution
            ).join();

            execution.setSqlQuery(sqlSolution);
            execution.setStatus(ExecutionStatus.SUCCESS);
            execution.setFailureReason(null);

        } catch (Exception e) {

            logger.warn("Execution {} failed: {}",
                    executionId, e.getMessage());

            execution.setStatus(ExecutionStatus.FAILED);
            execution.setFailureReason(e.getMessage());
        }

        execution.setCompletedAt(LocalDateTime.now());

        if (execution.getStartedAt() != null) {
            execution.setDurationMs(
                    Duration.between(
                            execution.getStartedAt(),
                            execution.getCompletedAt()
                    ).toMillis()
            );
        }

        executionRepository.save(execution);
    }

    // ===============================
    // RESILIENT METHODS
    // ===============================

    @CircuitBreaker(name = "webhookService", fallbackMethod = "submitFallback")
    @Retry(name = "webhookService")
    @TimeLimiter(name = "webhookService")
    public CompletableFuture<Void> submitSolutionWithResilience(
            String webhookUrl,
            String accessToken,
            String sqlQuery) {

        return CompletableFuture.supplyAsync(() -> {

            SolutionRequest solutionRequest =
                    new SolutionRequest(sqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity<SolutionRequest> entity =
                    new HttpEntity<>(solutionRequest, headers);

            restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return null;
        });
    }

    @CircuitBreaker(name = "webhookService", fallbackMethod = "generateFallback")
    @Retry(name = "webhookService")
    public WebhookResponse generateWebhookWithResilience() {

        WebhookRequest request =
                new WebhookRequest(userName, regNo, userEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WebhookRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<WebhookResponse> response =
                restTemplate.exchange(
                        webhookGenerateUrl,
                        HttpMethod.POST,
                        entity,
                        WebhookResponse.class
                );

        return response.getBody();
    }

    // ===============================
    // FALLBACK METHODS
    // ===============================

    private CompletableFuture<Void> submitFallback(
            String webhookUrl,
            String accessToken,
            String sqlQuery,
            Throwable t) {

        logger.error("Webhook submission fallback triggered: {}",
                t.getMessage());

        return CompletableFuture.failedFuture(
                new RuntimeException("Webhook submission failed", t)
        );
    }

    private WebhookResponse generateFallback(Throwable t) {

        logger.error("Webhook generation fallback triggered: {}",
                t.getMessage());

        throw new RuntimeException("Webhook generation failed", t);
    }
}