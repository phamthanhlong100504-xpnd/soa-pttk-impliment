# Analysis and Design — Business Process Automation Solution

> **Goal**: Analyze a specific business process and design a service-oriented automation solution (SOA/Microservices).
> Scope: 4–6 week assignment — focus on **one business process**, not an entire system.

**References:**
1. *Service-Oriented Architecture: Analysis and Design for Services and Microservices* — Thomas Erl (2nd Edition)
2. *Microservices Patterns: With Examples in Java* — Chris Richardson
3. *Bài tập — Phát triển phần mềm hướng dịch vụ* — Hung Dang (available in Vietnamese)

---

## Part 1 — Analysis Preparation

### 1.1 Business Process Definition

Describe or diagram the high-level Business Process to be automated.

- **Domain**: Du lịch (Tourism / Travel)
- **Business Process**: Đặt tour du lịch trực tuyến (Online Tour Booking)
- **Actors**: Khách hàng (Customer), Hệ thống thanh toán (Payment Gateway — PayOS), Quản trị viên (Admin)
- **Scope**: Quy trình từ khi khách hàng tìm kiếm tour, chọn lịch trình, đặt chỗ, thanh toán, xác nhận đặt tour, đến khi nhận vé điện tử qua email.

**Process Diagram:**

```mermaid
flowchart TD
    A[Khách hàng truy cập website] --> B[Tìm kiếm / Lọc tour]
    B --> C[Xem chi tiết tour & lịch trình]
    C --> D{Đăng nhập?}
    D -->|Chưa| E[Đăng nhập / Đăng ký tài khoản]
    E --> D
    D -->|Rồi| F[Chọn lịch khởi hành & nhập thông tin hành khách]
    F --> G[Hệ thống giữ chỗ tạm thời - Slot Block]
    G --> H[Tạo đơn đặt tour - Booking]
    H --> I[Thanh toán qua PayOS]
    I --> J{Thanh toán thành công?}
    J -->|Không| K[Hủy giữ chỗ & Hủy đơn]
    J -->|Có| L[Xác nhận đặt tour]
    L --> M[Cập nhật tồn kho - Confirm Slot]
    M --> N[Tạo vé PDF]
    N --> O[Gửi email xác nhận kèm vé]
    O --> P[Hoàn tất]
```

### 1.2 Existing Automation Systems

| System Name | Type | Current Role | Interaction Method |
|-------------|------|--------------|-------------------|
| PayOS | Payment Gateway | Xử lý thanh toán trực tuyến | REST API / Webhook |

> Ngoài PayOS, quy trình được thiết kế mới hoàn toàn từ đầu dưới dạng microservices.

### 1.3 Non-Functional Requirements

Non-functional requirements serve as input for identifying Utility Service and Microservice Candidates in step 2.7.

| Requirement    | Description |
|----------------|-------------|
| Performance    | Thời gian phản hồi API < 2s. Hệ thống xử lý workflow đặt tour bất đồng bộ qua Temporal để không block request. |
| Security       | JWT-based authentication (HS256). Kong API Gateway bảo vệ endpoint bằng JWT plugin. Mật khẩu hash bằng BCrypt. |
| Scalability    | Mỗi service có thể scale độc lập. Database per service pattern. Service discovery qua Eureka. |
| Availability   | Temporal đảm bảo workflow reliability — tự retry khi activity fail. SlotBlock có TTL tự động giải phóng chỗ bị "treo". Optimistic Locking chống overbooking. |

---

## Part 2 — REST/Microservices Modeling

### 2.1 Decompose Business Process & 2.2 Filter Unsuitable Actions

Decompose the process from 1.1 into granular actions. Mark actions unsuitable for service encapsulation.

| # | Action | Actor | Description | Suitable? |
|---|--------|-------|-------------|-----------|
| 1 | Tìm kiếm tour | Khách hàng | Tìm kiếm tour theo từ khóa, điểm khởi hành, ngày, thời lượng, giá | ✅ |
| 2 | Xem chi tiết tour | Khách hàng | Xem thông tin chi tiết tour, lịch trình ngày, lịch khởi hành | ✅ |
| 3 | Đăng ký tài khoản | Khách hàng | Tạo tài khoản mới với username, password, email, SĐT | ✅ |
| 4 | Đăng nhập | Khách hàng | Xác thực và nhận JWT access/refresh token | ✅ |
| 5 | Làm mới token | Khách hàng | Cấp lại access token từ refresh token | ✅ |
| 6 | Kiểm tra chỗ trống | Hệ thống | Kiểm tra số slot available của tour schedule | ✅ |
| 7 | Giữ chỗ tạm thời | Hệ thống | Tạo slot block với TTL (PENDING) để ngăn overbooking | ✅ |
| 8 | Tạo đơn đặt tour | Hệ thống | Lưu booking với trạng thái PENDING_PAYMENT, kèm passengers và optional services | ✅ |
| 9 | Thanh toán online | Khách hàng | Thanh toán qua PayOS, nhận webhook callback | ✅ |
| 10 | Xác nhận đơn đặt tour | Hệ thống | Cập nhật booking sang CONFIRMED sau khi thanh toán thành công | ✅ |
| 11 | Cập nhật tồn kho | Hệ thống | Chuyển slot block sang CONFIRMED, cập nhật confirmed_slots | ✅ |
| 12 | Tạo vé PDF | Hệ thống | Render booking ticket thành file PDF từ HTML template | ✅ |
| 13 | Gửi email xác nhận | Hệ thống | Gửi email chứa thông tin đặt tour và vé PDF cho khách | ✅ |
| 14 | Dọn dẹp slot hết hạn | Hệ thống | Scheduled job tự động giải phóng slot block đã EXPIRED | ✅ |
| 15 | Quyết định chọn tour nào | Khách hàng | Khách tự quyết định chọn tour phù hợp — mang tính chủ quan | ❌ |

> Actions marked ❌: manual-only, require human judgment, or cannot be encapsulated as a service.

### 2.3 Entity Service Candidates

Identify business entities and group reusable (agnostic) actions into Entity Service Candidates.

| Entity | Service Candidate | Agnostic Actions |
|--------|-------------------|------------------|
| Account | Account Service | Đăng ký, Đăng nhập, Làm mới token, Lấy thông tin tài khoản |
| Tour | Tour Service | Tìm kiếm tour, Xem chi tiết tour (kèm schedule, itinerary) |
| Inventory | Inventory Service | Kiểm tra chỗ trống, Giữ chỗ (slot block), Cập nhật slot block, Dọn dẹp slot hết hạn |
| Booking | Booking Service | Tạo booking, Xác nhận booking, Lấy booking theo ID, Xử lý webhook thanh toán |

### 2.4 Task Service Candidate

Group process-specific (non-agnostic) actions into a Task Service Candidate.

| Non-agnostic Action | Task Service Candidate |
|---------------------|------------------------|
| Điều phối toàn bộ luồng đặt tour (validate → create booking → block slot → payment → confirm → update inventory → notify) | **Booking-Tour Service** (Temporal Workflow Orchestrator) |

### 2.5 Identify Resources

Map entities/processes to REST URI Resources.

| Entity / Process | Resource URI |
|------------------|--------------|
| Account | `/api/v1/accounts/{accountId}` |
| Auth (Login, SignUp, Refresh) | `/api/v1/auth/log-in`, `/api/v1/auth/sign-up`, `/api/v1/auth/refresh` |
| Tour (search & detail) | `/api/v1/tours`, `/api/v1/tours/{slug}` |
| Inventory (available slots) | `/api/v1/inventory/{tour-schedule-id}` |
| Slot Block | `/api/v1/inventory/slot-blocks` |
| Booking | `/api/v1/bookings`, `/api/v1/bookings?id={id}` |
| Booking Confirm | `/api/v1/bookings/confirm` |
| Payment Webhook | `/api/v1/payments/webhook` |
| Booking Tour (Orchestration) | `/api/v1/booking-tour/booking`, `/api/v1/booking-tour/status/{idempotencyKey}` |
| Document (PDF ticket) | `/api/v1/documents/booking-tickets/show-ticket`, `/api/v1/documents/booking-tickets/file-pdf-ticket` |
| Notification (email) | `/api/v1/notifications/booking-tickets/send` |

### 2.6 Associate Capabilities with Resources and Methods

| Service Candidate | Capability | Resource | HTTP Method |
|-------------------|------------|----------|-------------|
| Account Service | Lấy thông tin tài khoản | `/api/v1/accounts/{accountId}` | GET |
| Account Service | Đăng nhập | `/api/v1/auth/log-in` | POST |
| Account Service | Đăng ký | `/api/v1/auth/sign-up` | POST |
| Account Service | Làm mới token | `/api/v1/auth/refresh` | POST |
| Tour Service | Tìm kiếm tour | `/api/v1/tours` | GET |
| Tour Service | Xem chi tiết tour | `/api/v1/tours/{slug}` | GET |
| Inventory Service | Lấy số chỗ trống | `/api/v1/inventory/{tour-schedule-id}` | GET |
| Inventory Service | Tạo slot block | `/api/v1/inventory/slot-blocks` | POST |
| Inventory Service | Cập nhật slot block | `/api/v1/inventory/slots-blocks` | PUT |
| Booking Service | Tạo booking | `/api/v1/bookings` | POST |
| Booking Service | Xác nhận booking | `/api/v1/bookings/confirm` | POST |
| Booking Service | Lấy booking | `/api/v1/bookings?id={id}` | GET |
| Booking Service | Xử lý payment webhook | `/api/v1/payments/webhook` | POST |
| Booking-Tour Service | Khởi tạo workflow đặt tour | `/api/v1/booking-tour/booking` | POST |
| Booking-Tour Service | Kiểm tra trạng thái workflow | `/api/v1/booking-tour/status/{key}` | GET |
| Document Service | Render vé PDF (inline) | `/api/v1/documents/booking-tickets/show-ticket` | POST |
| Document Service | Tải vé PDF | `/api/v1/documents/booking-tickets/file-pdf-ticket` | POST |
| Notification Service | Gửi email xác nhận | `/api/v1/notifications/booking-tickets/send` | POST |

### 2.7 Utility Service & Microservice Candidates

Based on Non-Functional Requirements (1.3) and Processing Requirements, identify cross-cutting utility logic or logic requiring high autonomy/performance.

| Candidate | Type (Utility / Microservice) | Justification |
|-----------|-------------------------------|---------------|
| Notification Service | Utility Service | Chức năng gửi email là cross-cutting, không thuộc business domain cụ thể nào. Được gọi bởi Booking-Tour Workflow. Chạy async để không block workflow. |
| Document Service | Utility Service | Tạo PDF là chức năng tiện ích, phục vụ nhiều loại document (vé tour, hóa đơn...). Tách riêng để scale độc lập khi tải render PDF cao. |
| API Gateway (Kong) | Utility Service | Xử lý cross-cutting concerns: routing, CORS, JWT authentication, rate limiting. Single entry point cho tất cả requests. |
| Eureka Server | Utility Service | Service Registry/Discovery — cho phép các service tìm thấy nhau trong mạng Docker mà không hardcode IP. |

### 2.8 Service Composition Candidates

Interaction diagram showing how Service Candidates collaborate to fulfill the business process.

```mermaid
sequenceDiagram
    participant Client as Frontend
    participant GW as Kong API Gateway
    participant BTS as Booking-Tour Service<br/>(Temporal Orchestrator)
    participant IS as Inventory Service
    participant BS as Booking Service
    participant PS as Payment (PayOS)
    participant DS as Document Service
    participant NS as Notification Service

    Client->>GW: POST /api/v1/booking-tour/booking
    GW->>BTS: Forward request

    Note over BTS: Temporal Workflow bắt đầu

    BTS->>IS: GET /inventory/{scheduleId} — Validate available slots
    IS-->>BTS: Available slots info

    BTS->>BS: POST /bookings — Create booking (PENDING_PAYMENT)
    BS-->>BTS: BookingResponse (id, code, totalPrice)

    BTS->>IS: POST /inventory/slot-blocks — Block slots (PENDING)
    IS-->>BTS: SlotBlockResponse (slotBlockId)

    BTS->>BS: POST /payments/webhook — Simulate payment
    BS-->>BTS: PaymentWebhookResponse (paymentId)

    Note over BTS: Thanh toán thành công → Cập nhật

    BTS->>BS: POST /bookings/confirm — Confirm booking (CONFIRMED)
    BS-->>BTS: BookingResponse (CONFIRMED)

    BTS->>IS: PUT /inventory/slots-blocks — Confirm slot block
    IS-->>BTS: UpdateSlotBlockResponse (confirmedSlots)

    BTS->>NS: POST /notifications/booking-tickets/send — Send confirmation email
    NS-->>BTS: Email sent successfully

    BTS-->>GW: Workflow completed
    GW-->>Client: 200 OK — Đã nhận được yêu cầu đặt tour
```

---

## Part 3 — Service-Oriented Design

### 3.1 Uniform Contract Design

Service Contract specification for each service. Full OpenAPI specs:
- [`docs/api-specs/account-service.yaml`](api-specs/account-service.yaml)
- [`docs/api-specs/booking-service.yaml`](api-specs/booking-service.yaml)
- [`docs/api-specs/tour-service.yaml`](api-specs/tour-service.yaml)
- [`docs/api-specs/inventory-service.yaml`](api-specs/inventory-service.yaml)
- [`docs/api-specs/booking-tour-service.yaml`](api-specs/booking-tour-service.yaml)
- [`docs/api-specs/document-service.yaml`](api-specs/document-service.yaml)
- [`docs/api-specs/notification-service.yaml`](api-specs/notification-service.yaml)

**Account Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/auth/log-in` | POST | application/json | 200, 400, 401 |
| `/api/v1/auth/sign-up` | POST | application/json | 200, 400, 409 |
| `/api/v1/auth/refresh` | POST | application/json | 200, 401 |
| `/api/v1/accounts/{accountId}` | GET | application/json | 200, 404 |

**Tour Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/tours` | GET | application/json | 200 |
| `/api/v1/tours/{slug}` | GET | application/json | 200, 404 |

**Booking Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/bookings` | POST | application/json | 200, 400 |
| `/api/v1/bookings` | GET | application/json | 200, 404 |
| `/api/v1/bookings/confirm` | POST | application/json | 200, 400, 404 |
| `/api/v1/payments/webhook` | POST | application/json | 200, 400 |

**Inventory Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/inventory/{tour-schedule-id}` | GET | application/json | 200, 404 |
| `/api/v1/inventory/slot-blocks` | POST | application/json | 200, 400, 409 |
| `/api/v1/inventory/slots-blocks` | PUT | application/json | 200, 400, 404 |

**Booking-Tour Service (Task/Orchestrator):**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/booking-tour/booking` | POST | application/json | 200, 400 |
| `/api/v1/booking-tour/status/{idempotencyKey}` | GET | application/json | 200, 404 |

**Document Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/documents/booking-tickets/show-ticket` | POST | application/pdf | 200, 400, 500 |
| `/api/v1/documents/booking-tickets/file-pdf-ticket` | POST | application/pdf | 200, 400, 500 |

**Notification Service:**

| Endpoint | Method | Media Type | Response Codes |
|----------|--------|------------|----------------|
| `/api/v1/notifications/booking-tickets/send` | POST | application/json | 200, 400, 500 |

### 3.2 Service Logic Design

Internal processing flow for each service.

**Account Service — Login Flow:**

```mermaid
flowchart TD
    A[Receive POST /auth/log-in] --> B{Username & Password valid?}
    B -->|Invalid format| C[Return 400 Bad Request]
    B -->|Valid format| D[Query DB by username]
    D --> E{Account exists?}
    E -->|No| F[Return 401 - Tài khoản không tồn tại]
    E -->|Yes| G{BCrypt password match?}
    G -->|No| H[Return 401 - Sai mật khẩu]
    G -->|Yes| I[Generate JWT Access + Refresh Token]
    I --> J[Return 200 - AccountResponse with tokens]
```

**Booking-Tour Service — Temporal Workflow (Saga Pattern):**

```mermaid
flowchart TD
    A[Nhận CreateBookingRequest] --> B[Activity: Validate Tour Schedule]
    B --> C{Còn chỗ?}
    C -->|Không| D[Throw Exception → Saga Compensate]
    C -->|Có| E[Activity: Create Booking - PENDING_PAYMENT]
    E --> F[Activity: Block Inventory Slot - PENDING]
    F --> G[Activity: Process Payment via PayOS]
    G --> H{Payment OK?}
    H -->|Không| I[Saga Compensate → Rollback]
    H -->|Có| J[Activity: Confirm Booking - CONFIRMED]
    J --> K[Activity: Update Slot Block - CONFIRMED]
    K --> L[Activity: Send Email Notification]
    L --> M[Workflow Complete]

    style D fill:#f66,color:#fff
    style I fill:#f66,color:#fff
    style M fill:#6c6,color:#fff
```

**Inventory Service — Slot Block Flow:**

```mermaid
flowchart TD
    A[Receive POST /slot-blocks] --> B{Tour Schedule exists?}
    B -->|No| C[Return 400 Error]
    B -->|Yes| D{Available slots >= requested amount?}
    D -->|No| E[Return 409 - Overbooking prevented]
    D -->|Yes| F[Optimistic Lock: Decrement available_slots]
    F --> G[Create SlotBlock entity - status PENDING]
    G --> H[Set expires_at = now + 15 min]
    H --> I[Return SlotBlockResponse]

    J[Scheduled Job] --> K[Query PENDING blocks where expires_at < now]
    K --> L[Mark as EXPIRED & restore available_slots]
```

**Tour Service — Search Flow:**

```mermaid
flowchart TD
    A[Receive GET /tours?q=...&departures=...&...] --> B[Build dynamic query]
    B --> C[Query DB with filters: name LIKE, departure, date, duration, price range]
    C --> D[Map to TourSearchResponse list]
    D --> E[Return 200 with tour list]
```
