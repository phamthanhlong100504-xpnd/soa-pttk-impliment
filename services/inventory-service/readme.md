# Inventory Service

> Quản lý tồn kho chỗ ngồi tour — kiểm tra slot trống, giữ chỗ tạm thời và xác nhận đặt chỗ. Chống overbooking bằng Optimistic Locking.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8083` |
| **Tech Stack** | Java 17, Spring Boot 3, Spring Data JPA, Liquibase, Quartz Scheduler, PostgreSQL |
| **Database** | `inventory_db` |
| **Migration** | Liquibase (changelog trong `src/main/resources/liquibase/`) |
| **Bảo vệ** | JWT qua Kong Gateway |

---

## Chức năng chính

- **Kiểm tra slot trống** — xem số chỗ available/blocked/confirmed của một tour schedule
- **Tạo slot block** — giữ chỗ tạm thời (PENDING) khi khách bắt đầu đặt tour
- **Xác nhận slot block** — chuyển trạng thái sang CONFIRMED sau thanh toán
- **Dọn dẹp slot hết hạn** — Scheduled Job tự động giải phóng slot block EXPIRED (quá TTL 15 phút)
- **Gửi notification** — tích hợp NotificationClient để cảnh báo tình trạng chỗ sắp hết

---

## API Endpoints

| Method | Endpoint | Mô tả | Auth |
|--------|----------|-------|------|
| `GET` | `/api/v1/inventory/{tour-schedule-id}` | Kiểm tra số chỗ trống | ✅ JWT |
| `POST` | `/api/v1/inventory/slot-blocks` | Giữ chỗ tạm thời (tạo slot block) | ✅ JWT |
| `PUT` | `/api/v1/inventory/slots-blocks` | Xác nhận slot block (sau thanh toán) | ✅ JWT |
| `GET` | `/health` | Health check | ❌ Public |

### Ví dụ: Kiểm tra chỗ trống

```bash
curl "http://localhost:8083/api/v1/inventory/uuid-tour-schedule-id" \
  -H "Authorization: Bearer <JWT_TOKEN>"
# Response: { totalSlots: 30, availableSlots: 25, blockedSlots: 3, confirmedSlots: 2 }
```

### Ví dụ: Giữ chỗ

```bash
curl -X POST http://localhost:8083/api/v1/inventory/slot-blocks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "tourScheduleId": "uuid-...",
    "customerId": "uuid-customer",
    "amount": 2,
    "bookingId": "uuid-booking"
  }'
```

---

## Data Models

> Quan hệ chính:  `TourSchedule (1) -> (1) Inventory`, `TourSchedule (1) -> (N) SlotBlock`, `Inventory (1) -> (N) InventoryHistory`.

### TourSchedule (`tour_schedules`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `tour_id` | UUID | FK → `tours.id` |
| `code` | String | Mã lịch khởi hành (unique) |
| `start_date` | Date | Ngày khởi hành |

> `TourSchedule` là thực thể gốc của tồn kho theo từng lịch khởi hành; mỗi `tour_schedule` có đúng 1 dòng `inventory`.

### Inventory (`inventory`)

| Field | Type | Mô tả |
|-------|------|-------|
| `tour_schedule_id` | UUID | PK & FK → TourSchedule |
| `tour_id` | UUID | FK → Tour (denormalized) |
| `total_slots` | Integer | Tổng số chỗ |
| `available_slots` | Integer | Chỗ còn trống |
| `blocked_slots` | Integer | Chỗ đang giữ tạm |
| `confirmed_slots` | Integer | Chỗ đã xác nhận |
| `low_stock_threshold` | Integer | Ngưỡng cảnh báo sắp hết chỗ |
| `version` | Long | **Optimistic Locking** — chống overbooking |

### SlotBlock (`slot_blocks`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `tour_schedule_id` | UUID | FK → TourSchedule |
| `customer_id` | UUID | ID khách hàng |
| `quantity` | Integer | Số chỗ giữ |
| `status` | Enum | `PENDING` / `CONFIRMED` / `EXPIRED` |
| `expires_at` | LocalDateTime | Thời gian hết hạn (PENDING → EXPIRED) |
| `booking_id` | UUID | ID booking liên kết |
| `created_at` | LocalDateTime | Thời điểm tạo |

### InventoryHistory (`inventory_history`)

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | UUID | Primary key |
| `tour_schedule_id` | UUID | FK → `inventory.tour_schedule_id` |
| `action_type` | Enum | `BLOCK` / `CONFIRM` / `EXPIRE` / `CANCEL` |
| `quantity_changed` | Integer | Số lượng thay đổi |
| `previous_available_slots` | Integer | Số chỗ trống trước thay đổi |
| `new_available_slots` | Integer | Số chỗ trống sau thay đổi |
| `reference_id` | UUID | Tham chiếu booking/process id |
| `actor` | String | Người/hệ thống thực hiện (`customerId`, `SYSTEM_JOB`) |
| `note` | String | Ghi chú nghiệp vụ |
| `created_at` | Instant | Thời điểm ghi nhận lịch sử |



---

## Cơ chế chống Overbooking

```
1. Khi POST /slot-blocks:
   - Kiểm tra available_slots >= amount
   - Dùng @Version (Optimistic Locking) để lock row Inventory
   - Decrement available_slots, Increment blocked_slots
   → Nếu có 2 request đồng thời: 1 thành công, 1 bị OptimisticLockException → retry

2. Scheduled Job (Quartz) chạy định kỳ:
   - Query: SELECT * FROM slot_blocks WHERE status='PENDING' AND expires_at < NOW()
   - Mark EXPIRED → Restore available_slots về inventory
```

---

## Database Migration (Liquibase)

```
liquibase/
├── master.xml
└── changelog/
    ├── 202608040821_create_db_inventory.xml
    ├── 202608041139_fix_fk.xml
    ├── 202608041936_update_column_table_inventory.xml
    ├── 202611041634_create_outbox_table.xml
    └── 202604080900_seed_initial_data.xml
```

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `SERVER_PORT` | Port service | `8083` |
| `DB_URL` | JDBC URL | `jdbc:postgresql://db:5432/inventory_db` |
| `DB_USER` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `123456` |

---

## Chạy local (Docker)

```bash
docker compose up --build inventory-service
```

## Kiểm tra service

```bash
curl http://localhost:8083/health
# → {"status":"ok"}
```

---

## Cấu trúc package

```
inventory_service/
├── client/
│   └── NotificationClient.java  # Gọi Notification Service
├── config/
│   └── SchedulerConfig.java     # Quartz Scheduler config
├── controller/
│   └── TourInventoryController.java
├── dto/
│   ├── request/                 # SlotBlockRequest, UpdateSlotBlockRequest, ...
│   └── response/                # AvailableResponse, SlotBlockResponse, ...
├── job/
│   └── SlotBlockCleanupJob.java  # Job dọn dẹp EXPIRED slots
├── model/
│   ├── Inventory.java
│   ├── SlotBlock.java
│   ├── Tour.java
│   ├── TourSchedule.java
│   ├── InventoryHistory.java
│   └── OutboxEntity.java
├── repository/
└── service/
    ├── TourInventoryService.java
    └── impl/TourInventoryServiceImpl.java
```

---

> **API Spec đầy đủ:** [`docs/api-specs/inventory-service.yaml`](../../docs/api-specs/inventory-service.yaml)
