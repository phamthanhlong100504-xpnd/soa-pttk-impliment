package com.vn.tech.inventory_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateSlotBlockResponse {
    private Integer confirmedSlots;
//    private Integer amount;
    private String actionType;
    private String bookingId;
}
