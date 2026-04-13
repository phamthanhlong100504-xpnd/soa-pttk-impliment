package com.vn.tech.booking_tour_service.activity;

import com.vn.tech.booking_tour_service.dto.request.initiate.*;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;

import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface BookingTaskActivities {
    @ActivityMethod
    void validateTourSchedule(ValidateTourScheduleRequest validateTourScheduleRequest);

    @ActivityMethod
    BookingResponse createBooking(CreateBookingRequest createBookingRequest);

    @ActivityMethod
    SlotBlockResponse blockInventorySlot(SlotBlockRequest slotBlockRequest);

//    @ActivityMethod
//    String generatePaymentUrl(GeneratePaymentUrlRequest generatePaymentUrlRequest);

    @ActivityMethod
    PaymentWebhookResponse payment(UUID bookingId);

    @ActivityMethod
    BookingResponse updateBooking(ConfirmBookingRequest confirmBookingRequest);

    @ActivityMethod
    UpdateSlotBlockResponse updateInventory(UpdateSlotBlockRequest updateSlotBlockRequest);

    @ActivityMethod
    void notification(EmailRequest request);
}
