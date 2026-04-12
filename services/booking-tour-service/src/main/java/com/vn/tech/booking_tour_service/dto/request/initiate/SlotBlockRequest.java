package com.vn.tech.booking_tour_service.dto.request.initiate;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SlotBlockRequest {
    private UUID tourScheduleId;
    private String customerId;
    private Integer amount;
    private String bookingId;
}
