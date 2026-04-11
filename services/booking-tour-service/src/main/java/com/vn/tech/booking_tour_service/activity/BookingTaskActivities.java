package com.vn.tech.booking_tour_service.activity;

import com.vn.tech.booking_tour_service.dto.request.initiate.*;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BookingTaskActivities {
    @ActivityMethod
    void validateTourSchedule(ValidateTourSchedule validateTourSchedule);

    @ActivityMethod
    BookingResponse createBooking(CreateBookingRequest createBookingRequest);

    @ActivityMethod
    SlotBlockResponse blockInventorySlot(SlotBlockRequest slotBlockRequest);

    @ActivityMethod
    void generatePaymentUrl(GeneratePaymentUrlRequest generatePaymentUrlRequest);
}
