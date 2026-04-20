package com.example.webhooksqlsolver.config;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class ResilienceMonitoringConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(ResilienceMonitoringConfig.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    public ResilienceMonitoringConfig(
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {

        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void registerEventListeners() {

        // ============================
        // Circuit Breaker Events
        // ============================
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(cb -> {

                    cb.getEventPublisher()
                            .onStateTransition(event ->
                                    logger.warn(
                                            "CircuitBreaker '{}' state changed from {} to {}",
                                            event.getCircuitBreakerName(),
                                            event.getStateTransition().getFromState(),
                                            event.getStateTransition().getToState()
                                    )
                            );

                    cb.getEventPublisher()
                            .onFailureRateExceeded(event ->
                                    logger.error(
                                            "CircuitBreaker '{}' failure rate exceeded threshold",
                                            event.getCircuitBreakerName()
                                    )
                            );

                    cb.getEventPublisher()
                            .onCallNotPermitted(event ->
                                    logger.error(
                                            "CircuitBreaker '{}' OPEN — call blocked",
                                            event.getCircuitBreakerName()
                                    )
                            );
                });

        // ============================
        // Retry Events
        // ============================
        retryRegistry.getAllRetries()
                .forEach(retry -> {

                    retry.getEventPublisher()
                            .onRetry(event ->
                                    logger.warn(
                                            "Retry '{}' attempt {} due to {}",
                                            event.getName(),
                                            event.getNumberOfRetryAttempts(),
                                            event.getLastThrowable().toString()
                                    )
                            );
                });

        logger.info("Resilience4j monitoring listeners registered");
    }
}
