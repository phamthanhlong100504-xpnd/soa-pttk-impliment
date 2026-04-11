package com.vn.tech.booking_tour_service.controller;

import com.vn.tech.booking_tour_service.dto.ApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.workflow.BookingTaskWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking-tour")
public class BookingTourController {

    @Autowired
    private WorkflowClient workflowClient;

    @PostMapping("/initiate")
    public ApiResponse<?> createBooking(@RequestBody CreateBookingRequest request) {

        String requestId = UUID.randomUUID().toString();

        BookingTaskWorkflow workflow = workflowClient.newWorkflowStub(
            BookingTaskWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("BOOKING_TASK_QUEUE")
                .setWorkflowId("Booking-" + requestId)
                .build());

        // Chạy Workflow bất đồng bộ (Async)
        WorkflowClient.start(workflow::initiateBooking, request);

        return ApiResponse.builder()
            .code(200)
            .message("Dat tour thanh cong")
            .build();
    }
}
