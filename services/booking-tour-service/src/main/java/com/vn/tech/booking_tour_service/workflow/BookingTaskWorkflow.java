package com.vn.tech.booking_tour_service.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.UUID;

@WorkflowInterface
public interface BookingTaskWorkflow {

    @WorkflowMethod
    void updateBooking(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount, String email);
}
