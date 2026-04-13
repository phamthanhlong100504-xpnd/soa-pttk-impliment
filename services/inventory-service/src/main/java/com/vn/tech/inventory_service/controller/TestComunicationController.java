package com.vn.tech.inventory_service.controller;

import com.vn.tech.inventory_service.client.NotificationClient;
import com.vn.tech.inventory_service.dto.request.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestComunicationController {

    private final NotificationClient notificationClient;

    @PostMapping("/test-flow") // Đổi tên path cho rõ ràng
    public ResponseEntity<String> testFlow() {
        // 1. Tạo request với ĐẦY ĐỦ các trường để pass qua Validation bên Notification
        EmailRequest email = EmailRequest.builder()
            .email("khoihv1@gmail.com")
            .name("Nguyễn Văn Khách") // BẮT BUỘC: Vì bên kia đang check "Tên người nhập"
            .subject("Xác nhận giữ chỗ Tour")
            .message("Hệ thống đã ghi nhận yêu cầu giữ chỗ của bạn.")
          /*  .bookingId("BK-2026-999") // Nên có để log/data khớp nhau
            .amount(2)       */          // Nên có
            .build();

        log.info("[Inventory] Đang chuẩn bị gọi Notification Service...");

        try {
            // 2. GỌI SANG NOTIFICATION SERVICE
            String result = notificationClient.sendEmail(email);

            log.info("[Inventory] Phản hồi từ Notification: {}", result);
            return ResponseEntity.ok("Flow hoạt động hoàn hảo: " + result);

        } catch (Exception e) {
            log.error("[Inventory] Lỗi khi gọi Notification: {}", e.getMessage());
            return ResponseEntity.status(500).body("Lỗi kết nối liên dịch vụ: " + e.getMessage());
        }
    }
}
