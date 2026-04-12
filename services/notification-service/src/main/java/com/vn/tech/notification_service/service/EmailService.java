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
    public void sendEmail(EmailRequest request) {
        try {
            log.info("Bắt đầu gửi email xác nhận vé tới: {}", request.getToEmail());

            // 1. Nhét dữ liệu từ DTO vào HTML Template
            Context context = new Context();
            context.setVariable("customerName", request.getCustomerName());
            context.setVariable("bookingId", request.getBookingId());
            context.setVariable("tourName", request.getTourName());
            context.setVariable("quantity", request.getQuantity());
            context.setVariable("confirmedSlots", request.getConfirmedSlots());
            context.setVariable("totalPrice", request.getTotalPrice());

            // Nhét cả mảng (List) vào để HTML tự lặp
            context.setVariable("passengers", request.getPassengers());
            context.setVariable("optionalServices", request.getOptionalServices());

            // Chữ "confirmation" phải khớp chính xác với tên file confirmation.html
            String htmlContent = templateEngine.process("confirmation", context);

            // 2. Cấu hình gửi mail định dạng HTML
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getToEmail());
            helper.setSubject(request.getSubject() != null ? request.getSubject() : "Xác nhận đặt vé Tour thành công - " + request.getBookingId());
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Đã gửi email thành công tới: {}", request.getToEmail());

        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", request.getToEmail(), e.getMessage());
        }
    }
}
