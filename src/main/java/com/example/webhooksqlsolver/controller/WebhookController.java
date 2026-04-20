package com.example.webhooksqlsolver.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.webhooksqlsolver.model.ExecutionResponse;
import com.example.webhooksqlsolver.model.ExecutionSummaryResponse;
import com.example.webhooksqlsolver.service.WebhookService;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runWebhook() {
        System.out.println("Webhook endpoint hit");

        try {
            webhookService.executeWebhookFlow();
            return ResponseEntity.ok("Webhook executed successfully");
        } catch (Exception e) {
            System.out.println("Exception caught in controller");
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/executions")
    public ResponseEntity<Page<ExecutionResponse>> getExecutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                webhookService.getExecutions(page, size)
        );
    }
    @GetMapping("/summary")
    public ResponseEntity<ExecutionSummaryResponse> getSummary() {
        return ResponseEntity.ok(webhookService.getExecutionSummary());
    }
}
