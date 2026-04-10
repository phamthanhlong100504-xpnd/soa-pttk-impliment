package com.vn.tech.booking_service.dto.request.booking;

import com.vn.tech.booking_service.common.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentWebhookRequest {
    private UUID bookingId;
    private String paymentId;
    private String idempotencyKey;
    private PaymentStatus paymentStatus;
    private Long amount;
}
