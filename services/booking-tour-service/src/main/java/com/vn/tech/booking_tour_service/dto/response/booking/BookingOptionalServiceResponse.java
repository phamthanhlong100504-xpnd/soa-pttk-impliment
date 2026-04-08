package com.vn.tech.booking_tour_service.dto.response.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingOptionalServiceResponse {
    private UUID id;
    private UUID optionalServiceId;
    private String serviceName;
    private int quantity;
    private String priceType;
    private Long unitPrice;
    private Long totalPrice;
}
