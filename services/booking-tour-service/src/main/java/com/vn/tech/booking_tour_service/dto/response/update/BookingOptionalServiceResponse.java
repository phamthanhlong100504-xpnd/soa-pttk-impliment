package com.vn.tech.booking_tour_service.dto.response.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingOptionalServiceResponse {
    private UUID id;
    private UUID optionalServiceId;
    private String serviceName;
    private int quantity;
    private String priceType;
    private Long unitPrice;
    private Long totalPrice;
}
