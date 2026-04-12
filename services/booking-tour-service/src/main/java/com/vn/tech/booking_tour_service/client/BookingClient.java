package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.response.create.ApiResponse;
import com.vn.tech.booking_tour_service.dto.response.update.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "booking-service",path = "/api/v1/bookings")
public interface BookingClient {

    @PostMapping("confirm")
    public ApiResponse<BookingResponse> confirmBooking(@RequestBody ConfirmBookingRequest confirmBookingRequest);
}
