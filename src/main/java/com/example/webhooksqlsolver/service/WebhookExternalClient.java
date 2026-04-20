package com.example.webhooksqlsolver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import com.example.webhooksqlsolver.model.WebhookRequest;
import com.example.webhooksqlsolver.model.WebhookResponse;

@Service
public class WebhookExternalClient {

    private final RestTemplate restTemplate;

    @Value("${webhook.generate.url}")
    private String webhookGenerateUrl;

    @Value("${app.user.name}")
    private String userName;

    @Value("${app.user.regNo}")
    private String regNo;

    @Value("${app.user.email}")
    private String userEmail;

    public WebhookExternalClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "webhookService", fallbackMethod = "generateFallback")
    @Retry(name = "webhookService")
    public WebhookResponse generateWebhook() {

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

    private WebhookResponse generateFallback(Throwable t) {
        throw new RuntimeException("Webhook generation failed after retries", t);
    }
}