package com.vn.tech.booking_tour_service.controller;


import com.vn.tech.booking_tour_service.dto.request.update.UpdateBookingTourRequest;
import com.vn.tech.booking_tour_service.dto.response.update.WorkflowResponse;
import com.vn.tech.booking_tour_service.dto.response.update.WorkflowStatusResponse;
import com.vn.tech.booking_tour_service.service.BookingTriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/booking-tour")
@RequiredArgsConstructor
public class UpdateBookingTourController {

//    private final UpdateBookingTourService updateBookingTourService;
    private final BookingTriggerService bookingTriggerService;

    @PostMapping("/update")
    public WorkflowResponse updateBookingTour(@RequestBody UpdateBookingTourRequest updateBookingRequest) {
        log.info("[BookingTour] - event : UpdateBooking with TourSchedule : {}",updateBookingRequest.getTourScheduleId());

        UUID tourScheduleId = updateBookingRequest.getTourScheduleId();
        String customerId = updateBookingRequest.getCustomerId();
        UUID slotBlockId = updateBookingRequest.getSlotBlockId();
        UUID bookingId = updateBookingRequest.getBookingId();
        String paymentId = updateBookingRequest.getPaymentId();
        Long amount = updateBookingRequest.getAmount();
        String email = updateBookingRequest.getEmail();

//        updateBookingTourService.updateBookingTour(tourScheduleId,customerId,slotBlockId,bookingId,paymentId,amount);
        WorkflowResponse response = bookingTriggerService.triggerUpdateBookingWorkflow(
            tourScheduleId, customerId, slotBlockId, bookingId, paymentId, amount,email
        );

        return response;
    }

    @GetMapping("/status/{bookingId}")
    public ResponseEntity<WorkflowStatusResponse> checkStatus(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingTriggerService.getWorkflowStatus(bookingId));
    }

}
