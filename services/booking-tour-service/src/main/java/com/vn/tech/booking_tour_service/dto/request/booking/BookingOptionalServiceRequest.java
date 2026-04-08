package com.vn.tech.booking_tour_service.dto.request.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingOptionalServiceRequest {
    private UUID optionalServiceId;
    private String serviceName;
    private int quantity;
    private String priceType;
    private Long unitPrice;
}
