package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.ApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "booking-service")
public interface BookingClient {
    @PostMapping("api/v1/bookings")
    public ApiResponse<BookingResponse> createBooking(@RequestBody CreateBookingRequest createBookingRequest);
}
