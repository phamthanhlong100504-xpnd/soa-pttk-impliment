# Account Service

> Quản lý tài khoản người dùng, xác thực và phân phối JWT token.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8081` |
| **Tech Stack** | Java 17, Spring Boot 3, Spring Security, PostgreSQL |
| **Database** | `account_db` |
| **Auth** | JWT (HS256) — BCrypt password hashing |

---

## Chức năng chính

- **Đăng ký tài khoản** — tạo tài khoản mới, lưu password với BCrypt
- **Đăng nhập** — xác thực username/password, cấp JWT access token + refresh token
- **Làm mới token** — cấp lại access token từ refresh token hợp lệ
- **Lấy thông tin tài khoản** — truy vấn thông tin account theo ID

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| `POST` | `/api/v1/auth/sign-up` | Đăng ký tài khoản mới | ❌ Public |
| `POST` | `/api/v1/auth/log-in` | Đăng nhập, nhận JWT token | ❌ Public |
| `POST` | `/api/v1/auth/refresh` | Làm mới access token | ❌ Public |
| `GET` | `/api/v1/accounts/{accountId}` | Lấy thông tin tài khoản | ✅ JWT |
| `GET` | `/health` | Health check | ❌ Public |

### Ví dụ: Đăng nhập

```bash
curl -X POST http://localhost:8000/api/v1/auth/log-in \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nguyenvana",
    "password": "123456"
  }'
```

### Ví dụ: Đăng ký

```bash
curl -X POST http://localhost:8000/api/v1/auth/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nguyenvana",
    "password": "123456",
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@gmail.com",
    "phoneNumber": "0912345678"
  }'
```

---

## Data Model

### AccountEntity (`tbl_account`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key (auto-generated) |
| `username` | String | Tên đăng nhập (unique) |
| `password` | String | Mật khẩu (BCrypt hash) |
| `fullName` | String | Họ và tên |
| `email` | String | Email |
| `phoneNumber` | String | Số điện thoại |
| `dateOfBirth` | Date | Ngày sinh |
| `role` | String | Vai trò (USER, ADMIN) |

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8081` |
| `DB_URL` | JDBC URL database | `jdbc:postgresql://db:5432/account_db` |
| `DB_USER` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `123456` |
| `JWT_SECRET` | JWT signing secret | *(xem .env)* |
| `JWT_EXPIRATION_ACCESS` | Access token TTL (ms) | `900000` (15 phút) |
| `JWT_EXPIRATION_REFRESH` | Refresh token TTL (ms) | `864000000` (10 ngày) |

---

## Chạy local (Docker)

```bash
# Từ thư mục gốc project
docker compose up --build account-service
```

## Kiểm tra service

```bash
curl http://localhost:8081/health
# → {"status":"ok"}
```

---

## Cấu trúc package

```
account_service/
├── config/
│   ├── SecurityConfig.java      # Spring Security config
│   └── TokenHelper.java         # JWT utility
├── controller/
│   ├── AuthController.java      # /api/v1/auth/*
│   ├── AccountController.java   # /api/v1/accounts/*
│   └── HealthController.java
├── dto/
│   ├── request/                 # LoginRequest, SignUpRequest, RefreshRequest
│   └── response/                # AccountResponse, TokenResponse
├── entity/
│   └── AccountEntity.java
├── exception/                   # AppException, ErrorCode, GlobalExceptionHandler
├── repository/
│   └── AccountRepository.java
└── service/
    ├── AuthService.java
    └── AccountService.java
```

---

> **API Spec đầy đủ:** [`docs/api-specs/account-service.yaml`](../../docs/api-specs/account-service.yaml)
