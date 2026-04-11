package com.vn.tech.booking_tour_service.dto.request.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UpdateBookingTourRequest {
    private UUID bookingTourId;
    private String status;
}
