package com.vn.tech.inventory_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SlotBlockResponse {
//    private UUID slotBlockId;
    private Integer previousAvailableSlots;
    private Integer newAvailableSlots;
    private Integer amount;
    private String actionType;
    private UUID slotBlockId;
}
