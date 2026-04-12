package com.vn.tech.booking_tour_service.dto.request.update;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateSlotBlockRequest
{
    private UUID tourScheduleId;
    private String customerId;
    private UUID slotBlockId;
}
