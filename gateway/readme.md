# API Gateway

## Overview

The API Gateway serves as the single entry point for all client requests. It routes incoming requests to the appropriate backend microservice.

## Responsibilities

- **Request routing**: Forward requests to the correct service
- **Authentication**: Validate tokens/credentials
- **Rate limiting**: Protect services from overload

## Tech Stack

| Component  | Choice             |
|------------|--------------------|
| Approach   | *(e.g., Nginx, Express, FastAPI, Kong, Traefik)* |

## Routing Table

| External Path        | Target Service | Internal URL                   |
|----------------------|----------------|--------------------------------|
| `/api/service-a/*`   | Service A      | `http://service-a:5000/*`      |
| `/api/service-b/*`   | Service B      | `http://service-b:5000/*`      |

## Running

```bash
# From project root
docker compose up gateway --build
```

## Configuration

The gateway uses Docker Compose networking. Services are accessible by their
service names defined in `docker-compose.yml` (e.g., `service-a`, `service-b`).

## Notes

- Use service names (not `localhost`) for upstream URLs inside Docker
- The gateway exposes port 8000 to the host
