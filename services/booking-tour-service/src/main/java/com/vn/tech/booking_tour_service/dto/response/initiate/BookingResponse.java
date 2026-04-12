package com.vn.tech.booking_tour_service.dto.response.initiate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vn.tech.booking_tour_service.common.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    private UUID id;
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Long totalPrice;
    private BookingStatus bookingStatus;

    private String paymentId;
    private LocalDateTime createdAt;

    private List<PassengerResponse> passengers;

    private List<BookingOptionalServiceResponse> optionalServices;
}
