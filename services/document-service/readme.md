# Document Service

> **Utility Service** — Tạo file PDF vé đặt tour từ HTML template sử dụng Flying Saucer (iText/XHTML renderer). Hỗ trợ xem inline và tải về.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8084` |
| **Tech Stack** | Java 17, Spring Boot 3, Flying Saucer PDF (XHTML Renderer), Thymeleaf-style HTML template |
| **Database** | ❌ Không có — stateless utility |
| **Output** | `application/pdf` |
| **Bảo vệ** | JWT qua Kong Gateway |

---

## Chức năng chính

- **Render vé tour PDF (inline)** — tạo PDF và trả về dạng inline để xem trực tiếp trên trình duyệt
- **Tải vé tour PDF** — tạo PDF và trả dạng attachment để tải về máy

PDF được render từ template HTML (`booking_ticket.html`) với font Arial hỗ trợ tiếng Việt.

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth | Content-Type |
|--------|----------|-------|------|--------------|
| `POST` | `/api/v1/documents/booking-tickets/show-ticket` | Render PDF (inline) | ✅ JWT | `application/pdf` |
| `POST` | `/api/v1/documents/booking-tickets/file-pdf-ticket` | Tải PDF | ✅ JWT | `application/pdf` |
| `GET` | `/health` | Health check | ❌ Public | `application/json` |

### Ví dụ: Tạo vé PDF

```bash
curl -X POST http://localhost:8084/api/v1/documents/booking-tickets/show-ticket \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "bookingId": "BK-20260501-001",
    "accountId": "uuid-...",
    "tourScheduleId": "uuid-...",
    "tourName": "Tour Đà Nẵng - Hội An 3N2Đ",
    "quantity": 2,
    "confirmedSlots": 2,
    "totalPrice": 5990000,
    "passengers": [
      {"fullName": "Nguyễn Văn A"}
    ],
    "optionalServices": [
      {"serviceName": "Bảo hiểm du lịch", "quantity": 2, "priceType": "PER_PERSON"}
    ]
  }' \
  --output ticket.pdf
```

---

## Request Body — BookingTicketRequest

| Field | Type | Mô tả |
|-------|------|-------|
| `bookingId` | String | Mã đơn đặt tour (hiển thị trên vé) |
| `accountId` | UUID | ID tài khoản khách hàng |
| `tourScheduleId` | UUID | ID lịch khởi hành |
| `tourName` | String | Tên tour |
| `quantity` | int | Số lượng hành khách |
| `confirmedSlots` | Integer | Số chỗ đã xác nhận trong inventory |
| `totalPrice` | Long | Tổng giá (VND) |
| `passengers` | Array | Danh sách hành khách `[{fullName}]` |
| `optionalServices` | Array | Dịch vụ tùy chọn `[{serviceName, quantity, priceType}]` |

---

## PDF Template

Template HTML nằm tại:
```
src/main/resources/templates/booking_ticket.html
```

Font tiếng Việt (Arial) tại:
```
src/main/resources/fonts/
├── arial.ttf
├── arialbd.ttf    # Bold
├── ariali.ttf     # Italic
└── ...
```

---

## Content-Disposition

| Endpoint | Header | Tên file |
|----------|--------|----------|
| `/show-ticket` | `inline` | `Bill_{accountId}_code{bookingId}.pdf` |
| `/file-pdf-ticket` | `attachment` | `Ve_Tour_{bookingId}.pdf` |

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8084` |

---

## Chạy local (Docker)

```bash
docker compose up --build document-service
```

## Kiểm tra service

```bash
curl http://localhost:8084/health
```

---

## Cấu trúc package

```
document_service/
├── controller/
│   └── BookingTicketController.java    # POST /api/v1/documents/booking-tickets/*
├── model/
│   └── BookingTicketRequest.java       # Request DTO (includes nested Passenger, OptionalService)
└── service/
    └── BookingTicketRenderService.java  # Flying Saucer PDF generation
```

---

> **API Spec đầy đủ:** [`docs/api-specs/document-service.yaml`](../../docs/api-specs/document-service.yaml)
