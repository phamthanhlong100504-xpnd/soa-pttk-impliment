package com.vn.tech.booking_tour_service.controller;

import com.vn.tech.booking_tour_service.dto.ApiResponseCreate;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.dto.response.update.WorkflowStatusResponse;
import com.vn.tech.booking_tour_service.service.BookingTriggerService;
import com.vn.tech.booking_tour_service.workflow.BookingTaskWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking-tour")
@RequiredArgsConstructor
public class BookingTourController {

    private final WorkflowClient workflowClient;
    private final BookingTriggerService bookingTriggerService;

    @PostMapping("/booking")
    public ApiResponseCreate<?> createBooking(@RequestBody CreateBookingRequest request) {

        String idempotencyKey = request.getIdempotencyKey();

        BookingTaskWorkflow workflow = workflowClient.newWorkflowStub(
            BookingTaskWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("BOOKING_TASK_QUEUE")
                .setWorkflowId("Booking-" + idempotencyKey)
                .build());

        // Chạy Workflow bất đồng bộ (Async)
//        WorkflowClient.start(workflow::, request);
        WorkflowClient.start(() -> workflow.bookingTour(
            request
        ));

        return ApiResponseCreate.builder()
            .code(200)
            .message("Đã nhận được yêu cầu đặt tour")
            .build();
    }

    @GetMapping("/status/{idempotencyKey}")
    public ResponseEntity<WorkflowStatusResponse> checkStatus(@PathVariable String idempotencyKey) {
        return ResponseEntity.ok(bookingTriggerService.getWorkflowStatus(idempotencyKey));
    }
}
