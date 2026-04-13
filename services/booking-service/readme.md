# Booking Service

> Quản lý đơn đặt tour, thông tin hành khách, dịch vụ tùy chọn và xử lý webhook thanh toán.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8080` |
| **Tech Stack** | Java 17, Spring Boot 3, Spring Data JPA, PostgreSQL |
| **Database** | `booking_db` |
| **Bảo vệ** | JWT validation qua Kong Gateway |

---

## Chức năng chính

- **Tạo booking** — lưu đơn đặt tour với trạng thái `PENDING_PAYMENT`, bao gồm danh sách hành khách và dịch vụ tùy chọn
- **Xác nhận booking** — chuyển trạng thái sang `CONFIRMED` sau khi thanh toán thành công
- **Lấy booking** — truy vấn thông tin đơn đặt tour theo ID
- **Xử lý payment webhook** — nhận callback từ PayOS khi thanh toán hoàn tất

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| `POST` | `/api/v1/bookings` | Tạo booking mới | ✅ JWT |
| `GET` | `/api/v1/bookings/{bookingId}` | Lấy booking theo ID | ✅ JWT |
| `POST` | `/api/v1/bookings/confirm` | Xác nhận booking | ✅ JWT |
| `POST` | `/api/v1/payments/webhook` | Nhận webhook thanh toán | ❌ Public |
| `GET` | `/health` | Health check | ❌ Public |

> ⚠️ Endpoint `/api/v1/payments/webhook` được mở public trong Kong để PayOS có thể gọi webhook callback.

### Ví dụ: Tạo booking

```bash
curl -X POST http://localhost:8000/api/v1/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "accountId": "uuid-...",
    "tourScheduleId": "uuid-...",
    "tourName": "Tour Đà Nẵng - Hội An 3N2Đ",
    "quantity": 2,
    "totalPrice": 5990000,
    "passengers": [
      {"fullName": "Nguyễn Văn A", "dateOfBirth": "1995-01-15", "passengerType": "ADULT"}
    ]
  }'
```

---

## Data Models

### BookingEntity (`tbl_bookings`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `bookingCode` | String | Mã đặt tour |
| `accountId` | UUID | ID tài khoản khách hàng |
| `tourScheduleId` | UUID | ID lịch khởi hành |
| `tourName` | String | Tên tour |
| `quantity` | int | Số lượng |
| `totalPrice` | Long | Tổng giá (VND) |
| `bookingStatus` | Enum | `PENDING_PAYMENT` / `CONFIRMED` / `CANCELLED` |
| `paymentId` | String | ID thanh toán từ PayOS |
| `createdAt` | LocalDateTime | Thời điểm tạo |

### PassengerEntity (`tbl_passengers`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `bookingId` | UUID | FK → booking |
| `fullName` | String | Họ và tên hành khách |
| `dateOfBirth` | LocalDate | Ngày sinh |
| `passengerType` | Enum | `ADULT` / `CHILD` / `INFANT` |

### BookingOptionalServiceEntity (`tbl_passengers`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `bookingId` | UUID | FK → booking |
| `optionalServiceId` | UUID | ID của dịch vụ tùy chọn |
| `serviceName` | String | Tên dịch vụ tại thời điểm đặt |
| `quantity` | int | Số lượng dịch vụ đã chọn |
| `priceType` | String | Loại giá (ví dụ: Per Person| Per Group| Per Day) |
| `unitPrice` | Long | Đơn giá của dịch vụ |
| `totalPrice` | Long | Tổng tiền cho dịch vụ |

### PaymentRecordEntity

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `bookingId` | UUID | FK → booking |
| `paymentId` | String | ID của dịch vụ tùy chọn |
| `idempotencyKey` | String | Khóa duy nhất để tránh xử lý thanh toán trùng lặp |
| `amount` | Long | Số tiền thanh toán |
| `status` | Enum | Trạng thái thanh toán (ví dụ: PENDING, SUCCESS, FAILED) |

> Lưu lịch sử giao dịch thanh toán liên kết với booking.

---

## Outbox Pattern

Service có `OutboxEntity` — sẵn sàng tích hợp **Transactional Outbox** với Kafka/Debezium để publish events (hiện tại chưa kích hoạt).

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8080` |
| `DB_URL` | JDBC URL | `jdbc:postgresql://db:5432/booking_db` |
| `DB_USER` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `123456` |

---

## Chạy local (Docker)

```bash
docker compose up --build booking-service
```

## Kiểm tra service

```bash
curl http://localhost:8080/health
# → {"status":"ok"}
```

---

## Cấu trúc package

```
booking_service/
├── common/
│   ├── BookingStatus.java       # PENDING_PAYMENT, CONFIRMED, CANCELLED
│   ├── PassengerType.java       # ADULT, CHILD, INFANT
│   └── PaymentStatus.java
├── controller/
│   ├── BookingController.java   # /api/v1/bookings
│   ├── PaymentController.java   # /api/v1/payments
│   └── HealthController.java
├── dto/
│   ├── request/booking/         # CreateBookingRequest, ConfirmBookingRequest, ...
│   └── response/booking/        # BookingResponse, PassengerResponse, ...
├── entity/
│   ├── BookingEntity.java
│   ├── PassengerEntity.java
│   ├── PaymentRecordEntity.java
│   ├── OutboxEntity.java
│   └── ...
├── exception/
├── repository/
└── service/
    ├── BookingService.java
    └── PaymentService.java
```

---

> **API Spec đầy đủ:** [`docs/api-specs/booking-service.yaml`](../../docs/api-specs/booking-service.yaml)
