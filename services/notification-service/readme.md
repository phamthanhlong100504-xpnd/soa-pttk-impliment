# Notification Service

> **Utility Service** — Gửi email xác nhận đặt tour cho khách hàng qua SMTP. Chạy bất đồng bộ (`@Async`) để không block workflow.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8085` |
| **Tech Stack** | Java 17, Spring Boot 3, JavaMailSender, Thymeleaf (email template), Spring Async |
| **Database** | ❌ Không có — stateless utility |
| **Email** | SMTP (cấu hình qua `.env`) |
| **Bảo vệ** | ❌ Internal service (không expose qua Kong) |

---

## Chức năng chính

- **Gửi email xác nhận đặt tour** — nhận thông tin booking từ Booking-Tour Workflow, render HTML email bằng Thymeleaf template (`confirmation.html`), gửi qua SMTP
- **Bất đồng bộ** — sử dụng `@Async` để gửi email không block response của workflow

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| `POST` | `/api/v1/notifications/booking-tickets/send` | Gửi email xác nhận | ❌ Internal |
| `GET` | `/health` | Health check | ❌ Public |

### Ví dụ: Gửi email

```bash
curl -X POST http://localhost:8085/api/v1/notifications/booking-tickets/send \
  -H "Content-Type: application/json" \
  -d '{
    "toEmail": "customer@example.com",
    "customerName": "Nguyễn Văn A",
    "subject": "Xác nhận đặt tour du lịch",
    "bookingId": "BK-20260501-001",
    "tourName": "Tour Đà Nẵng - Hội An 3N2Đ",
    "quantity": 2,
    "confirmedSlots": 2,
    "totalPrice": 5990000,
    "passengers": [
      {"fullName": "Nguyễn Văn A"}
    ],
    "optionalServices": []
  }'
```

### Response thành công

```json
{
  "status": "success",
  "message": "Email đã được đưa vào hàng đợi gửi thành công",
  "bookingId": "BK-20260501-001"
}
```

---

## Email Template

Template HTML Thymeleaf tại:
```
src/main/resources/templates/confirmation.html
```

Nội dung email bao gồm:
- Tên khách hàng, mã đặt tour
- Tên tour, số lượng chỗ, tổng giá
- Danh sách hành khách
- Dịch vụ tùy chọn (nếu có)

---

## Request Body — EmailRequest

| Field | Type | Mô tả |
|-------|------|-------|
| `toEmail` | String | Email người nhận (**bắt buộc**) |
| `customerName` | String | Tên khách hàng |
| `subject` | String | Tiêu đề email |
| `bookingId` | String | Mã đơn đặt tour |
| `accountId` | UUID | ID tài khoản |
| `tourScheduleId` | UUID | ID lịch khởi hành |
| `tourName` | String | Tên tour |
| `quantity` | int | Số lượng |
| `confirmedSlots` | Integer | Số chỗ đã xác nhận |
| `totalPrice` | Long | Tổng giá (VND) |
| `passengers` | Array | Danh sách hành khách `[{fullName}]` |
| `optionalServices` | Array | Dịch vụ tùy chọn `[{serviceName, quantity, priceType}]` |

---

## Cấu hình SMTP

Thêm vào `.env`:

```env
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_STARTTLS=true
```

> 💡 Với Gmail, sử dụng **App Password** (không dùng mật khẩu tài khoản chính).

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8085` |
| `MAIL_HOST` | SMTP host | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP port | `587` |
| `MAIL_USERNAME` | Email gửi | *(cần cấu hình)* |
| `MAIL_PASSWORD` | App password | *(cần cấu hình)* |

---

## Chạy local (Docker)

```bash
docker compose up --build notification-service
```

## Kiểm tra service

```bash
curl http://localhost:8085/health
```

---

## Cấu trúc package

```
notification_service/
├── config/
│   ├── AsyncConfig.java         # @EnableAsync, ThreadPoolTaskExecutor
│   └── SecurityConfig.java
├── controller/
│   └── BookingTicketController.java    # POST /api/v1/notifications/booking-tickets/send
├── dto/
│   └── EmailRequest.java               # Request DTO (nested Passenger, OptionalService)
└── service/
    └── EmailService.java               # JavaMailSender + Thymeleaf render
```

---

> **API Spec đầy đủ:** [`docs/api-specs/notification-service.yaml`](../../docs/api-specs/notification-service.yaml)
