package com.vn.tech.booking_tour_service.workflow;

import com.vn.tech.booking_tour_service.activity.BookingTaskActivities;
import com.vn.tech.booking_tour_service.dto.request.initiate.*;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.failure.TemporalFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

@WorkflowImpl
@Slf4j
public class BookingTaskWorkflowImpl implements BookingTaskWorkflow {
    private final BookingTaskActivities activities = Workflow.newActivityStub(
        BookingTaskActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .build())
            .build());

    @Override
    public void initiateBooking(CreateBookingRequest request) {
        Saga.Options sagaOptions = new Saga.Options.Builder()
            .setParallelCompensation(false)
            .build();
        Saga saga = new Saga(sagaOptions);

        try {
            log.info("initiate booking");

            log.info("validate tour schedule");
            ValidateTourScheduleRequest validateTourScheduleRequest = ValidateTourScheduleRequest.builder()
                .tourScheduleId(request.getTourScheduleId())
                .quantity(request.getQuantity())
                .build();

            activities.validateTourSchedule(validateTourScheduleRequest);

            log.info("valdate tour schedule success");

            log.info("create booking");

            BookingResponse bookingResponse = activities.createBooking(request);

//            saga.addCompensation(activities::cancelBooking, bookingResponse.getId());

            log.info("create booking success");

            log.info("create block slots");

            SlotBlockRequest slotReq = SlotBlockRequest.builder()
                .bookingId(bookingResponse.getId().toString())
                .tourScheduleId(request.getTourScheduleId())
                .amount(request.getQuantity())
                .build();
            SlotBlockResponse slotResponse = activities.blockInventorySlot(slotReq);

            log.info("block slot success");

            // Đăng ký bù: Nếu tạo link thanh toán lỗi, hãy giải phóng slot này
//            saga.addCompensation(activities::releaseInventorySlot, slotResponse.getId());

//            //Tạo link thanh toán PayOS
//            GeneratePaymentUrlRequest payRequest = GeneratePaymentUrlRequest.builder()
//                .bookingId(bookingResponse.getId())
//                .tourName(bookingResponse.getTourName())
//                .totalPrice(bookingResponse.getTotalPrice())
//                .build();
//            String paymentUrl = activities.generatePaymentUrl(payRequest);
//
//            if (paymentUrl == null) {
//                throw ApplicationFailure.newFailure("Không thể tạo link thanh toán", "PAYMENT_URL_NULL");
//            }
//
//            return paymentUrl; // Trả về link cho Controller để Redirect khách hàng
            log.info("payment");

            PaymentWebhookResponse paymentWebhookResponse = activities.payment(bookingResponse.getId());

            log.info("payment success");

        } catch (TemporalFailure e) {
            saga.compensate();
            throw e;
        }
    }
}
