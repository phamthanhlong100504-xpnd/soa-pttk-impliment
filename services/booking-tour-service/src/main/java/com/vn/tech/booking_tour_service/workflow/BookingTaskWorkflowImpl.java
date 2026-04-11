package com.vn.tech.booking_tour_service.workflow;

import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import io.temporal.spring.boot.WorkflowImpl;

@WorkflowImpl
public class BookingTaskWorkflowImpl implements BookingTaskWorkflow {
    @Override
    public void initiateBooking(CreateBookingRequest createBookingRequest) {

    }
}
