# Webhook SQL Problem Solver

A Spring Boot application that automatically processes webhook-based SQL problem solving on startup.

## Features

- **Automatic Startup Flow**: Initiates webhook generation immediately when the application starts
- **Dynamic Problem Assignment**: Determines SQL problem based on registration number (odd/even)
- **JWT Authentication**: Securely submits solutions using JWT tokens
- **RestTemplate Integration**: Uses Spring's RestTemplate for HTTP communication
- **Comprehensive Logging**: Detailed logging throughout the process

## How It Works

1. **Webhook Generation**: On startup, sends POST request to generate webhook with user details
2. **Problem Determination**: Based on last two digits of registration number:
   - Odd numbers → Question 1
   - Even numbers → Question 2
3. **Solution Submission**: Submits the SQL solution to the webhook URL using JWT authentication

## Configuration

The application uses the following default configuration:
- **Name**: John Doe
- **Registration Number**: REG12347 (ends in 47 - odd, so Question 1)
- **Email**: john@example.com

To modify these values, update the `WebhookService.java` file.

## Running the Application

```bash
./mvnw spring-boot:run
```

The application will automatically start the webhook flow upon startup. Check the logs to see the progress and results.

## API Endpoints Used

- **Generate Webhook**: `POST https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- **Submit Solution**: `POST https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`

## SQL Solutions

The application includes placeholder SQL solutions for both questions. Update the `SqlProblemSolver.java` file with the actual SQL queries based on the problem requirements from the Google Drive links.