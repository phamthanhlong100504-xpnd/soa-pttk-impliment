package com.vn.tech.booking_service.dto.response.booking;

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
public class PaymentWebhookResponse {
    private UUID bookingId;
    private String paymentId;
    private Long amount;
    private PaymentStatus status;
}
