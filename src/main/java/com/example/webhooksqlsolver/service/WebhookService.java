package com.example.webhooksqlsolver.service;

import com.example.webhooksqlsolver.model.SolutionRequest;
import com.example.webhooksqlsolver.model.WebhookRequest;
import com.example.webhooksqlsolver.model.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String WEBHOOK_GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SqlProblemSolver sqlProblemSolver;

    public void processWebhookFlow() {
        try {
            logger.info("Starting webhook flow...");

            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null) {
                logger.error("Failed to generate webhook");
                return;
            }

            logger.info("Webhook generated successfully");
            logger.info("Webhook URL: {}", webhookResponse.getWebhook());

            // Step 2: Solve SQL problem based on registration number
            String regNo = "22BCE11583"; 
            String sqlSolution = sqlProblemSolver.solveProblem(regNo);

            logger.info("SQL solution generated: {}", sqlSolution);

            // Step 3: Submit solution to the generated webhook
            submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlSolution);

            logger.info("Webhook flow completed successfully");

        } catch (Exception e) {
            logger.error("Error in webhook flow: {}", e.getMessage(), e);
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            // Create request body with your actual details
            WebhookRequest request = new WebhookRequest("Suhail Rajput", "22BCE11583", "suhailrajput325@gmail.com");

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            // Send POST request
            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                    WEBHOOK_GENERATE_URL,
                    HttpMethod.POST,
                    entity,
                    WebhookResponse.class
            );

            return response.getBody();

        } catch (Exception e) {
            logger.error("Error generating webhook: {}", e.getMessage(), e);
            return null;
        }
    }

    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            // Create solution request
            SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);

            // Set headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);

            // Create HTTP entity
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);

            // Send POST request to the GENERATED webhook
            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            logger.info("Solution submitted successfully. Response: {}", response.getBody());

        } catch (Exception e) {
            logger.error("Error submitting solution: {}", e.getMessage(), e);
        }
    }
}
