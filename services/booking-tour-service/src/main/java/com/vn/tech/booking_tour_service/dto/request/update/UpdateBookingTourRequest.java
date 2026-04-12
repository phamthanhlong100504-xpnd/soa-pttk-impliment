package com.vn.tech.booking_tour_service.dto.request.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data // Để có getter/setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingTourRequest {
    private UUID tourScheduleId;
    private String customerId;
    private UUID slotBlockId;
    private UUID bookingId;
    private String paymentId;
    private Long amount;
    private String email;
}
