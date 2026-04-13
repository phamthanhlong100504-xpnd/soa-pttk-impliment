package com.vn.tech.booking_tour_service.dto.response.initiate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotBlockResponse {
    //    private UUID slotBlockId;
    private Integer previousAvailableSlots;
    private Integer newAvailableSlots;
    private Integer amount;
    private String actionType;
    private UUID slotBlockId;
}
