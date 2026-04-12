package com.vn.tech.booking_tour_service.dto.request.initiate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotBlockRequest {
    private UUID tourScheduleId;
    private String customerId;
    private Integer amount;
    private String bookingId;
}
