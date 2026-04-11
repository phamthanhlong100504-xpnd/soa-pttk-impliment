package com.vn.tech.booking_tour_service.dto.response.initiate;

import com.vn.tech.booking_tour_service.common.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingSagaResponse {
    private String sagaId;
    private SagaStatus status;
    private String statusMessage;

    // Payment info
    private String paymentUrl;

    // Booking info (when completed)
    private BookingResponse booking;

    // Error info (when failed)
    private String errorMessage;
}
