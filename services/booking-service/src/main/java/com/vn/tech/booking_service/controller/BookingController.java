package com.vn.tech.booking_service.controller;

import com.vn.tech.booking_service.dto.ApiResponse;
import com.vn.tech.booking_service.dto.request.booking.ConfirmBookingRequest;
import com.vn.tech.booking_service.dto.request.booking.CreateBookingRequest;
import com.vn.tech.booking_service.dto.response.booking.BookingResponse;
import com.vn.tech.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public ApiResponse<BookingResponse> createBooking(@RequestBody CreateBookingRequest createBookingRequest) {
        return ApiResponse.<BookingResponse>builder()
            .code(200)
            .message("Tạo Booking thành công")
            .result(bookingService.createBooking(createBookingRequest))
            .build();
    }

    @PostMapping("confirm")
    public ApiResponse<BookingResponse> confirmBooking(@RequestBody ConfirmBookingRequest confirmBookingRequest) {
        return ApiResponse.<BookingResponse>builder()
            .code(200)
            .message("Xác nhận Booking thành công")
            .result(bookingService.confirmBooking(confirmBookingRequest))
            .build();
    }

}
