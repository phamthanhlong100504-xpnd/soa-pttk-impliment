package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.ApiResponseCreate;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.PaymentWebhookRequest;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "booking-service")
public interface BookingClient {
    @PostMapping("api/v1/bookings")
    public ApiResponseCreate<BookingResponse> createBooking(@RequestBody CreateBookingRequest createBookingRequest);

    @GetMapping("api/v1/bookings")
    public BookingResponse getBooking(@RequestParam UUID id);

    @PostMapping("api/v1/payments/webhook")
    public ApiResponseCreate<PaymentWebhookResponse> handlePaymentWebhook(@RequestBody PaymentWebhookRequest paymentWebhookRequest);

    @PostMapping("api/v1/bookings/confirm")
    public ApiResponseCreate<BookingResponse> confirmBooking(@RequestBody ConfirmBookingRequest confirmBookingRequest);

    @PostMapping("api/v1/bookings/cancel")
    public ApiResponseCreate<BookingResponse> cancelBooking(@RequestBody ConfirmBookingRequest cancelBookingRequest);
}
