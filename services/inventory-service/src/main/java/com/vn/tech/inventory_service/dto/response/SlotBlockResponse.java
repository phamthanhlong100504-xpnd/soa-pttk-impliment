package com.vn.tech.inventory_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlotBlockResponse {
    private Integer previousAvailableSlots;
    private Integer newAvailableSlots;
    private Integer amount;
    private String actionType;
}
