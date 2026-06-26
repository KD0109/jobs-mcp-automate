# EU / UAE Job Automation MCP

This starter project provides a Spring Boot MCP service that can:

- search public job sources for Europe and UAE
- rank matching roles for a backend engineer profile
- generate a short application draft
- persist applications in PostgreSQL
- run twice a day with a scheduler

## Run locally with Docker

```bash
docker compose up --build
```

This starts both PostgreSQL and the Spring Boot app. The app will be available at http://localhost:8080.

## Daily scheduling

The job discovery is scheduled with the `JOBS_SCHEDULER_CRON` environment variable.

Default value:

```text
0 0 9,17 * * *
```

That runs the discovery flow at 09:00 and 17:00 every day.

## CI

A GitHub Actions workflow is included in [.github/workflows/ci.yml](.github/workflows/ci.yml).

## Freeware providers

The app is configured to use GitHub Jobs by default, which does not require API keys.

## MCP examples

### List tools
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

## Notes

- The project uses official API integrations when configured.
- The current fallback provider enables local testing without blocking accounts.
- Avoid scraping LinkedIn or other sites that may ban automation.
