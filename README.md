# 🌏 Tour Booking Microservices

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](docker-compose.yml)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Temporal](https://img.shields.io/badge/Temporal-1.23.1-000000?logo=temporal&logoColor=white)](https://temporal.io)

> Hệ thống đặt tour du lịch trực tuyến theo kiến trúc **Microservices** — từ tìm kiếm tour, đặt chỗ, thanh toán, đến gửi vé điện tử qua email. Sử dụng **Temporal Workflow (Saga Pattern)** để đảm bảo toàn vẹn dữ liệu phân tán.

---

## 👥 Team Members

| Name | Student ID | Role | Contribution |
|------|------------|------|--------------|
|      |            |      |              |

---

## 🧩 Business Process

**Domain:** Du lịch (Tourism)

Hệ thống **đặt tour du lịch trực tuyến**:

1. Khách hàng tìm kiếm & xem chi tiết tour
2. Đăng nhập và chọn lịch khởi hành, nhập thông tin hành khách
3. Hệ thống giữ chỗ tạm thời (Slot Block) và tạo đơn đặt tour
4. Khách thanh toán
5. Hệ thống xác nhận đặt tour, cập nhật tồn kho
6. Gửi **vé PDF** qua email cho khách hàng

> 📄 Phân tích chi tiết: [`docs/analysis-and-design.md`](docs/analysis-and-design.md)

---

## 🏗️ Architecture

 [`docs/asset/architecture.png`](docs/asset/architecture.png)

### Danh sách Services

| Component | Chức năng | Tech Stack | Port |
|-----------|-----------|------------|------|
| **Frontend** | Giao diện đặt tour | React + Vite | 3001 |
| **Kong API Gateway** | Routing, JWT Auth, CORS, Rate Limit | Kong 3.x | 8000 / 8001 |
| **Account Service** | Đăng ký, đăng nhập, JWT token | Spring Boot 3, JPA | 8081 |
| **Tour Service** | Tìm kiếm, chi tiết tour | Spring Boot 3, JPA | 8082 |
| **Booking Service** | Quản lý đơn đặt tour, thanh toán | Spring Boot 3, JPA | 8080 |
| **Inventory Service** | Tồn kho slot, chống overbooking | Spring Boot 3, Liquibase, Quartz | 8083 |
| **Booking-Tour Service** | Orchestrate workflow (Saga) | Spring Boot 3, Temporal SDK | 8090 |
| **Document Service** | Tạo vé tour PDF | Spring Boot 3, Flying Saucer | 8084 |
| **Notification Service** | Gửi email xác nhận | Spring Boot 3, JavaMail, Thymeleaf | 8085 |
| **Eureka Server** | Service Registry & Discovery | Spring Boot 3, Netflix Eureka | 8761 |
| **Temporal Server** | Workflow engine (Saga/retry) | Temporal 1.23.1 | 7233 |
| **Temporal UI** | Dashboard giám sát workflow | Temporal UI 2.21.3 | 8091 |
| **PostgreSQL** | Database (multi-DB) | PostgreSQL 16 Alpine | 5432 |

> 📐 Kiến trúc chi tiết: [`docs/architecture.md`](docs/architecture.md)

---

## 🚀 Getting Started

### Yêu cầu

- [Docker Desktop](https://docs.docker.com/get-docker/) >= 24.x
- [Docker Compose](https://docs.docker.com/compose/) >= 2.x

### Cài đặt & Chạy

```bash
# 1. Clone repository
git clone <your-repo-url>
cd <project-folder>

# 2. Tạo file cấu hình môi trường
cp .env.example .env

# 3. Build và khởi động toàn bộ hệ thống
docker compose up --build
```

> ⏳ Lần đầu build có thể mất 5–10 phút do tải Maven dependencies.

### Kiểm tra hệ thống

```bash
# Frontend
curl http://localhost:3001

# API Gateway (Kong)
curl http://localhost:8000

# Các services
curl http://localhost:8081/health   # Account Service
curl http://localhost:8082/health   # Tour Service
curl http://localhost:8080/health   # Booking Service
curl http://localhost:8083/health   # Inventory Service
curl http://localhost:8090/health   # Booking-Tour Service
curl http://localhost:8084/health   # Document Service
curl http://localhost:8085/health   # Notification Service

# Eureka Dashboard
open http://localhost:8761

# Temporal UI
open http://localhost:8091
```

### Lệnh hữu ích (Makefile)

```bash
make up          # Khởi động tất cả services
make up-d        # Khởi động nền (detached)
make down        # Dừng tất cả services
make logs        # Xem log toàn bộ
make logs-service s=booking-tour-service  # Log 1 service cụ thể
make clean       # Xóa containers, volumes, images
make status      # Xem trạng thái services
```

---

## 🔑 Authentication

Hệ thống sử dụng **JWT (HS256)** được cấp bởi Account Service và validate tại Kong Gateway.

```bash
# 1. Đăng ký
curl -X POST http://localhost:8000/api/v1/auth/sign-up \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "123456", "fullName": "Test User", "email": "test@example.com"}'

# 2. Đăng nhập → lấy access token
curl -X POST http://localhost:8000/api/v1/auth/log-in \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "123456"}'

# 3. Dùng token cho các API yêu cầu auth
curl http://localhost:8000/api/v1/tours \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 📋 Booking Workflow

Quy trình đặt tour được điều phối bởi **Temporal Workflow (Saga Pattern)**:

```
POST /api/v1/booking-tour/booking
    │
    ▼
[1] Validate Tour Schedule (Inventory)
    │
    ▼
[2] Create Booking → PENDING_PAYMENT (Booking Service)
    │
    ▼
[3] Block Slots → PENDING (Inventory Service)
    │
    ▼
[4] Process Payment
    ▼
[5] Confirm Booking → CONFIRMED (Booking Service)
    │
    ▼
[6] Confirm Slot → CONFIRMED (Inventory Service)
    │
    ▼
[7] Send Email + PDF Ticket (Notification + Document Service)
```

> Nếu bất kỳ bước nào thất bại → **Saga tự động rollback** các bước trước.

---

## 📂 Project Structure

```
mid-project/
├── frontend/                   # React + Vite frontend
├── gateway/                    # Kong API Gateway config (kong.yml)
├── services/
│   ├── account-service/        # Auth & Account management
│   ├── booking-service/        # Booking & Payment webhook
│   ├── booking-tour-service/   # Temporal Workflow Orchestrator
│   ├── document-service/       # PDF ticket generator
│   ├── eureka-server/          # Service Registry
│   ├── inventory-service/      # Slot inventory management
│   ├── notification-service/   # Email notification
│   └── tour-service/           # Tour catalog
├── docs/
│   ├── analysis-and-design.md  # SOA/Erl analysis
│   ├── analysis-and-design-ddd.md  # DDD analysis
│   ├── architecture.md         # System architecture
│   └── api-specs/              # OpenAPI 3.0 specs (7 files)
├── init-db/                    # PostgreSQL multi-DB init script
├── scripts/                    # Utility scripts
├── docker-compose.yml          # Orchestration
├── Makefile                    # Developer commands
└── .env.example                # Environment template
```

---

## 📚 API Documentation

| Service | OpenAPI Spec |
|---------|-------------|
| Account Service | [`docs/api-specs/account-service.yaml`](docs/api-specs/account-service.yaml) |
| Booking Service | [`docs/api-specs/booking-service.yaml`](docs/api-specs/booking-service.yaml) |
| Tour Service | [`docs/api-specs/tour-service.yaml`](docs/api-specs/tour-service.yaml) |
| Inventory Service | [`docs/api-specs/inventory-service.yaml`](docs/api-specs/inventory-service.yaml) |
| Booking-Tour Service | [`docs/api-specs/booking-tour-service.yaml`](docs/api-specs/booking-tour-service.yaml) |
| Document Service | [`docs/api-specs/document-service.yaml`](docs/api-specs/document-service.yaml) |
| Notification Service | [`docs/api-specs/notification-service.yaml`](docs/api-specs/notification-service.yaml) |

---

## 🔧 Environment Variables

Xem file [`.env.example`](.env.example) để biết danh sách đầy đủ. Các biến quan trọng:

| Biến | Mô tả |
|------|-------|
| `DB_USER` / `DB_PASSWORD` | PostgreSQL credentials |
| `JWT_SECRET_KEY` | JWT signing secret (HS256) |
| `JWT_EXPIRATION_ACCESS` | Access token TTL (ms) |
| `JWT_EXPIRATION_REFRESH` | Refresh token TTL (ms) |
| `EUREKA_URL` | Eureka server URL |

---

## 📖 Tài liệu

- [`docs/analysis-and-design.md`](docs/analysis-and-design.md) — Phân tích & Thiết kế (SOA approach)
- [`docs/analysis-and-design-ddd.md`](docs/analysis-and-design-ddd.md) — Phân tích & Thiết kế (DDD approach)
- [`docs/architecture.md`](docs/architecture.md) — Kiến trúc hệ thống
- [`docs/api-specs/`](docs/api-specs/) — OpenAPI 3.0 Specifications

---

## 📜 License

This project uses the [MIT License](LICENSE).