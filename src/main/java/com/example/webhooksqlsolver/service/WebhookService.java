// package com.example.webhooksqlsolver.service;

// import java.time.LocalDateTime;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import com.example.webhooksqlsolver.entity.User;
// import com.example.webhooksqlsolver.entity.WebhookExecution;
// import com.example.webhooksqlsolver.exception.BadRequestException;
// import com.example.webhooksqlsolver.model.ExecutionResponse;
// import com.example.webhooksqlsolver.model.SolutionRequest;
// import com.example.webhooksqlsolver.model.WebhookRequest;
// import com.example.webhooksqlsolver.model.WebhookResponse;
// import com.example.webhooksqlsolver.repository.UserRepository;
// import com.example.webhooksqlsolver.repository.WebhookExecutionRepository;
// execution.setStatus(ExecutionStatus.PENDING);

// @Service
// public class WebhookService {

//     private static final Logger logger =
//             LoggerFactory.getLogger(WebhookService.class);

//     private final RestTemplate restTemplate;
//     private final SqlProblemSolver sqlProblemSolver;
//     private final WebhookExecutionRepository executionRepository;
//     private final UserRepository userRepository;
//     private final AsyncExecutionService asyncExecutionService;

//     @Value("${webhook.generate.url}")
//     private String webhookGenerateUrl;

//     @Value("${app.user.name}")
//     private String userName;

//     @Value("${app.user.regNo}")
//     private String regNo;

//     @Value("${app.user.email}")
//     private String userEmail;

//     @Autowired
//     public WebhookService(
//             RestTemplate restTemplate,
//             SqlProblemSolver sqlProblemSolver,
//             WebhookExecutionRepository executionRepository,
//             UserRepository userRepository,
//             AsyncExecutionService asyncExecutionService) {

//         this.restTemplate = restTemplate;
//         this.sqlProblemSolver = sqlProblemSolver;
//         this.executionRepository = executionRepository;
//         this.userRepository = userRepository;
//         this.asyncExecutionService = asyncExecutionService;
//     }

//     // ✅ Wrapper method used by controller
//         public void executeWebhookFlow() {

//         logger.info("Submitting webhook job to background executor");

//         // 🔐 Get authenticated user
//         String email = SecurityContextHolder.getContext()
//                 .getAuthentication()
//                 .getName();

//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() ->
//                         new RuntimeException("Authenticated user not found"));

//         // 🧱 Create execution record FIRST
//         WebhookExecution execution = new WebhookExecution();
//         execution.setExecutedAt(LocalDateTime.now());
//         execution.setRegNo(regNo);
//         execution.setStatus(ExecutionStatus.PENDING);
//         execution.setUser(user);

//         executionRepository.save(execution);

//         // 🚀 Trigger async job
//         asyncExecutionService.processWebhookAsync(execution.getId());
//         }
//     public void processWebhookFlow() {
//         try {
//             logger.info("Starting webhook flow...");

//             WebhookResponse webhookResponse = generateWebhook();

//             if (webhookResponse == null) {
//                 logger.error("Failed to generate webhook");
//                 saveExecution(null, "FAILED");
//                 return;
//             }

//             logger.info("Webhook generated successfully");
//             logger.info("Webhook URL: {}", webhookResponse.getWebhook());

//             String sqlSolution =
//                     sqlProblemSolver.solveProblem(regNo);

//             logger.info("SQL solution generated: {}", sqlSolution);

//             boolean success = submitSolution(
//                     webhookResponse.getWebhook(),
//                     webhookResponse.getAccessToken(),
//                     sqlSolution
//             );

//             String status = success ? "SUCCESS" : "FAILED";

//             saveExecution(sqlSolution, status);

//             logger.info("Webhook flow completed");

//         } catch (Exception e) {
//             logger.error(
//                     "Error in webhook flow: {}",
//                     e.getMessage(),
//                     e
//             );

//             saveExecution(null, "FAILED");
//         }
//     }

//         private void saveExecution(String sqlQuery, String status) {
//         try {
//                 // 🔐 Get authenticated user email from SecurityContext
//                 String email = SecurityContextHolder.getContext()
//                         .getAuthentication()
//                         .getName();

//                 // 🔎 Fetch user from database
//                 User user = userRepository.findByEmail(email)
//                         .orElseThrow(() ->
//                                 new RuntimeException("Authenticated user not found"));

//                 WebhookExecution execution = new WebhookExecution();
//                 execution.setExecutedAt(LocalDateTime.now());
//                 execution.setRegNo(regNo);
//                 execution.setSqlQuery(sqlQuery);
//                 execution.getStatus().name(),
//                 execution.setUser(user);   // 🔥 Attach user here

//                 executionRepository.save(execution);

//                 logger.info("Execution saved to database");

//         } catch (Exception ex) {
//                 logger.error("Failed to save execution: {}", ex.getMessage());
//         }
//         }

//     private WebhookResponse generateWebhook() {
//         try {
//             WebhookRequest request =
//                     new WebhookRequest(userName, regNo, userEmail);

//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_JSON);

//             HttpEntity<WebhookRequest> entity =
//                     new HttpEntity<>(request, headers);

//             ResponseEntity<WebhookResponse> response =
//                     restTemplate.exchange(
//                             webhookGenerateUrl,
//                             HttpMethod.POST,
//                             entity,
//                             WebhookResponse.class
//                     );

//             return response.getBody();

//         } catch (Exception e) {
//             logger.error(
//                     "Error generating webhook: {}",
//                     e.getMessage(),
//                     e
//             );
//             return null;
//         }
//     }

//     private boolean submitSolution(
//             String webhookUrl,
//             String accessToken,
//             String sqlQuery
//     ) {
//         try {
//             SolutionRequest solutionRequest =
//                     new SolutionRequest(sqlQuery);

//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_JSON);
//             headers.set("Authorization",
//                     "Bearer " + accessToken);

//             HttpEntity<SolutionRequest> entity =
//                     new HttpEntity<>(solutionRequest, headers);

//             ResponseEntity<String> response =
//                     restTemplate.exchange(
//                             webhookUrl,
//                             HttpMethod.POST,
//                             entity,
//                             String.class
//                     );

//             logger.info(
//                     "Solution submitted successfully. Response: {}",
//                     response.getBody()
//             );

//             return true;

//         } catch (Exception e) {
//             logger.error(
//                     "Error submitting solution: {}",
//                     e.getMessage(),
//                     e
//             );
//             return false;
//         }
//     }

//         public Page<ExecutionResponse> getExecutions(int page, int size) {

//         if (page < 0 || size <= 0) {
//                 throw new BadRequestException(
//                         "Page must be >= 0 and size must be > 0"
//                 );
//         }

//         Pageable pageable = PageRequest.of(page, size);

//         // 🔐 Get authenticated user email
//         String email = SecurityContextHolder.getContext()
//                 .getAuthentication()
//                 .getName();

//         User user = userRepository.findByEmail(email)
//                 .orElseThrow(() ->
//                         new RuntimeException("Authenticated user not found"));

//         Page<WebhookExecution> executions =
//                 executionRepository.findByUser(user, pageable);

//         // 🔄 Map Entity → DTO
//         return executions.map(execution ->
//                 new ExecutionResponse(
//                         execution.getId(),
//                         execution.getRegNo(),
//                         execution.getSqlQuery(),
//                         execution.getStatus().name(),
//                         execution.getExecutedAt(),
//                         execution.getUser().getEmail()
//                 )
//         );
//         }
// }


package com.example.webhooksqlsolver.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.webhooksqlsolver.entity.ExecutionStatus;
import com.example.webhooksqlsolver.entity.User;
import com.example.webhooksqlsolver.entity.WebhookExecution;
import com.example.webhooksqlsolver.exception.BadRequestException;
import com.example.webhooksqlsolver.model.ExecutionResponse;
import com.example.webhooksqlsolver.model.ExecutionSummaryResponse;
import com.example.webhooksqlsolver.repository.UserRepository;
import com.example.webhooksqlsolver.repository.WebhookExecutionRepository;

@Service
public class WebhookService {

    private static final Logger logger =
            LoggerFactory.getLogger(WebhookService.class);

    private final WebhookExecutionRepository executionRepository;
    private final UserRepository userRepository;
    private final AsyncExecutionService asyncExecutionService;

    @Autowired
    public WebhookService(
            WebhookExecutionRepository executionRepository,
            UserRepository userRepository,
            AsyncExecutionService asyncExecutionService) {

        this.executionRepository = executionRepository;
        this.userRepository = userRepository;
        this.asyncExecutionService = asyncExecutionService;
    }

    // 🚀 Entry point used by controller
        @Value("${app.user.regNo}")
        private String regNo;

        public void executeWebhookFlow() {

        logger.info("Submitting webhook job to background executor");

        // 🔐 Get authenticated user
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        // 🧱 Create execution record
        WebhookExecution execution = new WebhookExecution();

        execution.setExecutedAt(LocalDateTime.now());
        execution.setRegNo(regNo);                     // ✅ Store regNo
        execution.setStatus(ExecutionStatus.PENDING); // ✅ Enum-based status
        execution.setUser(user);

        // Optional but clean initialization
        execution.setStartedAt(null);
        execution.setCompletedAt(null);
        execution.setDurationMs(null);
        execution.setSqlQuery(null);

        execution = executionRepository.save(execution);

        logger.info("Execution record created with ID: {}", execution.getId());

        // 🚀 Trigger async processing
        asyncExecutionService.processWebhookAsync(execution.getId());
        }

        public ExecutionSummaryResponse getExecutionSummary() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        long total = executionRepository.countByUser(user);
        long success = executionRepository.countByUserAndStatus(user, ExecutionStatus.SUCCESS);
        long failure = executionRepository.countByUserAndStatus(user, ExecutionStatus.FAILED);

        double successRate = total == 0 ? 0 :
                ((double) success / total) * 100;

        Double avgDuration = executionRepository
                .findAverageDurationByUser(user);

        return new ExecutionSummaryResponse(
                total,
                success,
                failure,
                successRate,
                avgDuration == null ? 0 : avgDuration
        );
        }




    public Page<ExecutionResponse> getExecutions(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new BadRequestException(
                    "Page must be >= 0 and size must be > 0"
            );
        }

        Pageable pageable = PageRequest.of(page, size);

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        Page<WebhookExecution> executions =
                executionRepository.findByUser(user, pageable);

        return executions.map(execution ->
                new ExecutionResponse(
                        execution.getId(),
                        execution.getRegNo(),
                        execution.getSqlQuery(),
                        execution.getStatus().name(),
                        execution.getExecutedAt(),
                        execution.getUser().getEmail(),
                        execution.getFailureReason()
                )
        );
    }
}