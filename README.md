# EU / UAE Job Automation MCP

A polished Spring Boot + MCP prototype for intelligent, recruiter-friendly job discovery across Europe and the UAE.

This project demonstrates a practical end-to-end workflow for modern hiring automation:

- discover public job opportunities from multiple providers
- focus on backend engineering and product-oriented roles
- rank and structure results for relevance
- generate a draft application package
- persist the workflow in a clean, extensible backend

It is designed as both a technical portfolio piece and a useful demo for teams exploring AI-assisted recruiting workflows.

## Why this stands out

This repository is not just a toy example. It combines:

- Spring Boot backend architecture
- MCP-style tool exposure for automation workflows
- real-world job provider integration
- scheduler-driven daily execution
- a clean path for future expansion into CRM, outreach, or application automation

For EU recruiters and hiring teams, the value is clear: faster discovery, better targeting, and a more structured candidate journey.

## Architecture at a glance

- Backend: Java 21 + Spring Boot 3
- API layer: REST + MCP-style endpoints
- Data: PostgreSQL / H2 for local testing
- Automation: scheduled background discovery
- Providers: GitHub Jobs, Adzuna, Jooble (when configured)

## Quick start

### 1) Run with Docker

```bash
docker compose up --build
```

Then open:

- http://localhost:8080/actuator/health
- http://localhost:8080/mcp

### 2) Local development

```bash
./mvnw spring-boot:run
```

## Example MCP calls

### List available tools

```bash
curl -X POST http://localhost:8080/mcp \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

### Search jobs

```bash
curl -X POST http://localhost:8080/mcp \
  -H 'Content-Type: application/json' \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"search_jobs","arguments":{"country":"EU","limit":5}}}'
```

## Configuration

The project uses environment variables for deployment-friendly configuration.

Key settings include:

- DB_URL / DB_USERNAME / DB_PASSWORD
- JOBS_SCHEDULER_CRON
- JOBS_TARGET_COUNTRIES
- JOBS_TARGET_LOCATIONS
- JOOBLE_API_KEY / ADZUNA_APP_ID / ADZUNA_APP_KEY

## Demo UI

A lightweight preview page is included in the ui folder for showcasing the idea to stakeholders.

Open:

```bash
# from the repo root
python -m http.server 8000 --directory ui
```

Then visit http://localhost:8000.

## Notes

- The current implementation favors public and compliant provider access where possible.
- It is intentionally designed to be extended into a richer recruiting workflow.
- The codebase is suitable for demonstrating strong engineering fundamentals to recruiters, hiring managers, or product teams.

## LinkedIn-ready summary

Built a Spring Boot-based job automation prototype that discovers relevant opportunities across Europe and the UAE, structures them through an MCP-style workflow, and prepares application drafts for backend engineering roles. This project highlights practical software engineering, API integration, automation, and product-minded thinking for modern recruiting workflows.
