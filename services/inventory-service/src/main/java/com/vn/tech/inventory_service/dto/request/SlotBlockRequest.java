package com.vn.tech.inventory_service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SlotBlockRequest {
    private UUID tourScheduleId;
    private String customerId;
    private Integer amount;
    private String bookingId;
}
