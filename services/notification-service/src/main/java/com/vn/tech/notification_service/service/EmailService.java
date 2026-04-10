package com.vn.tech.notification_service.service;

import com.vn.tech.notification_service.dto.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine; // Tiêm Thymeleaf vào đây

    @Async // Đánh dấu chạy ngầm
    public void sendEmail(EmailRequest emailRequest) {
        try {
            log.info("Bắt đầu gửi email tới: {}", emailRequest.getEmail());

            // 1. Nhét dữ liệu từ DTO vào HTML Template
            Context context = new Context();
            context.setVariable("customerName", emailRequest.getName());
//            context.setVariable("bookingId", emailRequest.getBookingId());
//            context.setVariable("amount", emailRequest.getAmount());

            // Chữ "confirmation" phải khớp chính xác với tên file confirmation.html
            String htmlContent = templateEngine.process("confirmation", context);

            // 2. Cấu hình gửi mail định dạng HTML
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailRequest.getEmail());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(htmlContent, true); // true = Báo cho Gmail biết đây là HTML

            mailSender.send(message);
            log.info("Gửi email thành công tới: {}", emailRequest.getEmail());

        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", emailRequest.getEmail(), e.getMessage());
        }
    }
}
