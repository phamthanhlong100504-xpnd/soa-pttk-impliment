# Tour Service

> Quản lý danh mục tour du lịch — tìm kiếm tour theo nhiều tiêu chí và xem chi tiết tour kèm lịch trình.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8082` |
| **Tech Stack** | Java 17, Spring Boot 3, Spring Data JPA, Spring Data Auditing, PostgreSQL |
| **Database** | `tour_db` |
| **Bảo vệ** | ❌ Không yêu cầu JWT (public read) |

---

## Chức năng chính

- **Tìm kiếm tour** — lọc theo từ khóa, điểm khởi hành, ngày, thời lượng (số ngày), khoảng giá
- **Xem chi tiết tour** — lấy thông tin đầy đủ gồm mô tả, danh sách lịch khởi hành (`TourSchedule`), và lịch trình ngày (`TourItinerary`)

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| `GET` | `/api/v1/tours` | Tìm kiếm tour (có filter) | ❌ Public |
| `GET` | `/api/v1/tours/{slug}` | Xem chi tiết tour | ❌ Public |
| `GET` | `/health` | Health check | ❌ Public |

### Query Parameters — Tìm kiếm tour

| Param | Type | Mô tả |
|-------|------|-------|
| `q` | String | Từ khóa tìm kiếm (tên tour) |
| `departures` | String | Điểm khởi hành |
| `start_date` | Date | Ngày khởi hành (ISO: `YYYY-MM-DD`) |
| `duration_days` | Integer | Số ngày tour |
| `min_price` | Decimal | Giá tối thiểu |
| `max_price` | Decimal | Giá tối đa |

### Ví dụ: Tìm tour 3 ngày từ Hà Nội

```bash
curl "http://localhost:8000/api/v1/tours?q=Đà+Nẵng&departures=Hà+Nội&duration_days=3"
```

### Ví dụ: Xem chi tiết tour theo slug

```bash
curl "http://localhost:8000/api/v1/tours/tour-da-nang-hoi-an-3n2d"

# Với X-Tour-Id header (tùy chọn, tăng tốc query)
curl "http://localhost:8000/api/v1/tours/tour-da-nang-hoi-an-3n2d" \
  -H "X-Tour-Id: uuid-..."
```

---

## Data Models

### Tour (`tours`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `slug` | String | URL-friendly identifier (unique) |
| `name` | String | Tên tour |
| `description` | TEXT | Mô tả chi tiết |
| `destinationId` | UUID | ID điểm đến |
| `departureId` | UUID | ID điểm khởi hành |
| `basePrice` | Decimal | Giá cơ bản |
| `durationDays` | Integer | Số ngày |
| `durationNights` | Integer | Số đêm |
| `status` | String | Trạng thái tour (ACTIVE/INACTIVE) |
| `averageRating` | Decimal | Điểm đánh giá trung bình |
| `reviewCount` | Integer | Số lượng đánh giá |
| `createdAt` | Instant | Auto-filled bởi Spring Auditing |
| `updatedAt` | Instant | Auto-filled bởi Spring Auditing |

### TourSchedule (`tour_schedules`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `tourId` | UUID | FK → Tour |
| `code` | String | Mã lịch khởi hành (unique) |
| `startDate` | LocalDate | Ngày khởi hành |
| `endDate` | LocalDate | Ngày về |
| `price` | Decimal | Giá cho lịch này |
| `maxParticipants` | Integer | Số khách tối đa |
| `minParticipants` | Integer | Số khách tối thiểu |
| `status` | String | Trạng thái (AVAILABLE/FULL/CANCELLED) |

### TourItinerary (`tour_itineraries`)

| Field | Type | Mô tả |
|-------|------|-------|
| `dayNumber` | Integer | Ngày thứ mấy trong tour |
| `title` | String | Tiêu đề ngày |
| `description` | TEXT | Mô tả hoạt động trong ngày |

---

## Dữ liệu mẫu

Dữ liệu seed cho tour service được chuẩn bị tại:
- `services/tour-service/schema.sql` — Tạo bảng
- `services/tour-service/seed-test.sql` — Dữ liệu mẫu
- File gốc từ thư mục root: `tour-data` (JSON)

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8082` |
| `DB_URL` | JDBC URL | `jdbc:postgresql://db:5432/tour_db` |
| `DB_USER` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `123456` |

---

## Chạy local (Docker)

```bash
docker compose up --build tour-service
```

## Kiểm tra service

```bash
curl http://localhost:8082/health
# → {"status":"ok"}
```

---

## Cấu trúc package

```
tour_services/
├── controller/
│   ├── TourController.java      # /api/v1/tours
│   └── HealthController.java
├── dto/
│   ├── TourSearchResponse.java
│   ├── TourDetailResponse.java
│   ├── TourResponse.java
│   ├── TourScheduleResponse.java
│   └── TourItineraryResponse.java
├── entity/
│   ├── Tour.java
│   ├── TourSchedule.java
│   └── TourItinerary.java
├── repository/
│   ├── TourRepository.java
│   ├── TourScheduleRepository.java
│   └── TourItineraryRepository.java
└── service/
    ├── TourService.java
    └── impl/TourServiceImpl.java
```

---

> **API Spec đầy đủ:** [`docs/api-specs/tour-service.yaml`](../../docs/api-specs/tour-service.yaml)
