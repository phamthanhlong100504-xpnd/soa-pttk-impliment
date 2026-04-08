package com.vn.tech.booking_tour_service.dto.request.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateBookingRequest {
    private UUID tourScheduleId;
    private UUID accountId;
    private String tourName;
    private int quantity;
    private Long totalPrice;

    List<PassengerRequest> passengerRequests;
    List<BookingOptionalServiceRequest>  bookingOptionalServiceRequests;
}
