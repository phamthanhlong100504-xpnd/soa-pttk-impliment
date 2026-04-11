package com.vn.tech.booking_tour_service.dto.request.initiate;

import lombok.Data;

import java.util.UUID;

@Data
public class SlotBlockRequest {
    private UUID tourScheduleId;
    private String customerId;
    private Integer amount;
    private String bookingId;
}
