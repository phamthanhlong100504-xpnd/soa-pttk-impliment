package com.vn.tech.booking_service.controller;

import com.vn.tech.booking_service.dto.ApiResponse;
import com.vn.tech.booking_service.dto.request.booking.PaymentWebhookRequest;
import com.vn.tech.booking_service.dto.response.booking.PaymentWebhookResponse;
import com.vn.tech.booking_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("webhook")
    public ApiResponse<PaymentWebhookResponse> handlePaymentWebhook(@RequestBody PaymentWebhookRequest paymentWebhookRequest) {
        return ApiResponse.<PaymentWebhookResponse>builder()
            .code(200)
            .message("Xử lý webhook thành công")
            .result(paymentService.processPaymentWebhook(paymentWebhookRequest))
            .build();
    }
}
