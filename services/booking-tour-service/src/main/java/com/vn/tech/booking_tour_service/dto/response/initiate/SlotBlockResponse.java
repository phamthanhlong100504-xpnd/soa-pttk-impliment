package com.vn.tech.booking_tour_service.dto.response.initiate;

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
