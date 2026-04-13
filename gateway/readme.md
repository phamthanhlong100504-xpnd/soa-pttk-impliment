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
| Approach   | Kong |

## Routing Table

| External Path        | Target Service | Internal URL                   |
|----------------------|----------------|--------------------------------|
| `/api/v1/bookings/*`   | Booking Service     | `http://booking-service:8080/*`      |
| `/api/v1/accounts/*`   | Account Service     | `http://account-service:8081/*`      |
| `/api/v1/tours/*`   | Tour Service     | `http://tour-service:8082/*`      |
| `/api/v1/inventory/*`   | Inventory Service     | `http://inventory-service:8083/*`      |
| `/api/v1/booking-tour-service/*`   | Inventory Service     | `http://booking-tour-service:8090/*`      |

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
