package com.vn.tech.notification_service.controller;

import com.vn.tech.notification_service.dto.EmailRequest;
import com.vn.tech.notification_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications/booking-tickets")
public class BookingTicketController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailRequest emailRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (emailRequest.getToEmail() == null || emailRequest.getToEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email người nhận không được để trống");
            }

            // Gọi Service gửi mail (Nếu lỗi nó sẽ nhảy thẳng xuống block catch)
            emailService.sendEmail(emailRequest);

            response.put("status", "success");
            response.put("message", "Email đã được đưa vào hàng đợi gửi thành công");
            response.put("bookingId", emailRequest.getBookingId());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi hệ thống khi gửi mail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
