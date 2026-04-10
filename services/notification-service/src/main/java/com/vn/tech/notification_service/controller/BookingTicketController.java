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
            // Validate input
            if (emailRequest.getEmail() == null || emailRequest.getEmail().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Email không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            if (emailRequest.getName() == null || emailRequest.getName().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Tên người nhập không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            if (emailRequest.getSubject() == null || emailRequest.getSubject().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Tiêu đề email không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            if (emailRequest.getMessage() == null || emailRequest.getMessage().trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Nội dung email không được để trống");
                return ResponseEntity.badRequest().body(response);
            }

            // Send email
            emailService.sendEmail(emailRequest);

            response.put("status", "success");
            response.put("message", "Email đã được gửi thành công");
            response.put("email", emailRequest.getEmail());
            response.put("name", emailRequest.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
