package com.vn.tech.booking_service.dto.request.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConfirmBookingRequest {
    private UUID bookingId;
    private String paymentId;
    private Long amount;
}
