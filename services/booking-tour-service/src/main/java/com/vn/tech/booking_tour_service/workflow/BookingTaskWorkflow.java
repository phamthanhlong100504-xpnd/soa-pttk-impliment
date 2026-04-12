package com.vn.tech.booking_tour_service.workflow;

import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BookingTaskWorkflow {
    @WorkflowMethod
    void initiateBooking(CreateBookingRequest createBookingRequest);

    @WorkflowMethod
    void updateBooking(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount, String email);
}
