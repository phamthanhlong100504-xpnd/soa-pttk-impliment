package com.vn.tech.booking_tour_service.dto.request.initiate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ValidateTourSchedule {
    private UUID tourScheduleI;
    private int quantity;
}
