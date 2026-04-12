package com.vn.tech.booking_tour_service.dto.response.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSlotBlockResponse {
    private Integer confirmedSlots;
    //    private Integer amount;
    private String actionType;
    private String bookingId;
}

