package com.vn.tech.booking_tour_service.dto.request.update;

import com.vn.tech.booking_tour_service.dto.response.initiate.BookingOptionalServiceResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PassengerResponse;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmailRequest {
    private String toEmail;
    private String customerName;
    private String subject;

    // Dữ liệu chi tiết của Tour
    private String bookingId;
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Integer confirmedSlots;
    private Long totalPrice;

    private List<PassengerResponse> passengers;
    private List<BookingOptionalServiceResponse> optionalServices;

}
