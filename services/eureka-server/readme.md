# Eureka Server

> **Infrastructure Service** — Service Registry & Discovery. Cho phép các microservice đăng ký và tìm thấy nhau trong mạng Docker mà không cần hardcode địa chỉ IP.

## Thông tin

| Thuộc tính | Giá trị |
|-----------|---------|
| **Port** | `8761` |
| **Tech Stack** | Java 17, Spring Boot 3, Netflix Eureka Server |
| **Database** | ❌ Không có — in-memory registry |
| **Dashboard** | `http://localhost:8761` |

---

## Chức năng chính

- **Service Registration** — nhận đăng ký từ tất cả microservices khi khởi động
- **Heartbeat Monitoring** — theo dõi health của các service qua heartbeat định kỳ
- **Service Discovery** — cho phép services tìm nhau bằng tên (service name) thay vì IP

---

## Dashboard

Truy cập Eureka Dashboard để xem danh sách service đang hoạt động:

```
http://localhost:8761
```

Dashboard hiển thị:
- Danh sách instances đã đăng ký
- Trạng thái UP/DOWN
- Zone và metadata

---

## Services đăng ký với Eureka

| Service | Tên đăng ký |
|---------|-------------|
| Account Service | `account-service` |
| Booking Service | `booking-service` |
| Tour Service | `tour-service` |
| Inventory Service | `inventory-service` |
| Booking-Tour Service | `booking-tour-service` |
| Document Service | `document-service` |
| Notification Service | `notification-service` |

---

## Cấu hình môi trường

| Biến | Mô tả | Default |
|------|-------|---------|
| `EUREKA_HOSTNAME` | Hostname của Eureka server | `eureka-server` |
| `EUREKA_SERVER_PORT` | Port | `8761` |

---

## Chạy local (Docker)

Eureka Server được khởi động đầu tiên (Kong phụ thuộc vào nó):

```bash
docker compose up --build eureka-server
```

## Kiểm tra Registry

```bash
# Xem danh sách service đã đăng ký (JSON format)
curl http://localhost:8761/eureka/apps

# Kiểm tra health
curl http://localhost:8761/actuator/health
```

---

## Client Configuration

Các service kết nối với Eureka bằng config trong `application.yaml`:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    hostname: ${spring.application.name}
```

---

> ℹ️ Trong môi trường Docker Compose, các service dùng tên container (`eureka-server`) để kết nối, không dùng `localhost`.
