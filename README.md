# 🚀 QueryNexus – Automated SQL Challenge Solver

QueryNexus is a Spring Boot backend application that automatically processes SQL challenges received via webhook, generates solutions, and submits them using JWT authentication.

The project was originally built for a hiring challenge and is now upgraded into a production-style backend project suitable for placement portfolios.


## 📌 Features

- Automatic webhook processing
- SQL challenge detection
- SQL solution generation
- JWT-based solution submission
- Startup execution using CommandLineRunner
- REST client communication
- Structured logging
- Clean service-based architecture

## 🧱 Tech Stack

- Java 17
- Spring Boot 3.2
- Maven
- REST APIs
- RestTemplate
- JWT Authentication
- Jackson JSON Mapper
- SLF4J Logging

Current version runs stateless without database.

---

## 🏗️ Project Architecture

Application Startup
↓
Webhook Generated
↓
SQL Problem Identified
↓
Solution Generated
↓
Solution Submitted via API

### Package Structure
│
├── WebhookSqlSolverApplication
├── component
│ └── StartupRunner
├── config
│ └── RestTemplateConfig
├── service
│ ├── WebhookService
│ └── SqlProblemSolver
└── model
├── WebhookRequest
├── WebhookResponse
└── SolutionRequest

---

## ▶️ How to Run Locally

### Prerequisites
- Java 17+
- Maven

### Steps

### Clone repository:

git clone repo-url> = (https://github.com/Suhail-8800/QueryNexus.git)
Move into project:

cd QueryNexus


Run application:

mvn spring-boot:run
Application executes automatically on startup.

### 📡 Application Flow

Application starts

Startup runner triggers webhook call

SQL challenge received

Solution generated

Solution submitted via JWT-authenticated API

### 🔜 Planned Improvements

REST endpoints for manual execution

Database integration for storing challenges

Docker containerization

Unit & integration testing

Deployment-ready configuration

Monitoring & logging upgrades

## API Endpoints

### Run Webhook Flow
POST /api/webhook/run

Triggers webhook generation, SQL solving, solution submission,
and stores execution logs.

### Get Execution History
GET /api/webhook/executions?page=0&size=5

Returns paginated execution logs.

Parameters:
- page: page number (default 0)
- size: records per page (default 5)

### 👨‍💻 Author

Suhail Rajput
Computer Science Student | Backend Developer

GitHub: https://github.com/Suhail-8800

LinkedIn: https://www.linkedin.com/in/suhail-rajput-64158722b/

### ⭐ Purpose

This project demonstrates backend automation, API integration, and service-layer architecture suitable for backend developer roles.

