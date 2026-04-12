package com.vn.tech.booking_tour_service.activity;

import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.update.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BookingTaskActivities {

    @ActivityMethod
    BookingResponse updateBooking(ConfirmBookingRequest confirmBookingRequest);

    @ActivityMethod
    UpdateSlotBlockResponse updateInventory(UpdateSlotBlockRequest updateSlotBlockRequest);

    @ActivityMethod
    void notification(EmailRequest request);
}
