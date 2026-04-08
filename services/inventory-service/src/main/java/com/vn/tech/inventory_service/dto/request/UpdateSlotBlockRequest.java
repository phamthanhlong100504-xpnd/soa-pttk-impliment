package com.vn.tech.inventory_service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateSlotBlockRequest
{
    private UUID tourScheduleId;
    private String customerId;
}
