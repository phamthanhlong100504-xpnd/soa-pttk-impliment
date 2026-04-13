package com.vn.tech.booking_tour_service.workflow.impl;

import com.vn.tech.booking_tour_service.activity.BookingTaskActivities;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.SlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.ValidateTourScheduleRequest;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import com.vn.tech.booking_tour_service.workflow.BookingTaskWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.TemporalFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@WorkflowImpl(taskQueues = "BookingTaskQueue")
@Slf4j
public class BookingTaskWorkflowImpl implements BookingTaskWorkflow {

//    private final ActivityOptions options = ActivityOptions.newBuilder()
//        .setStartToCloseTimeout(Duration.ofMinutes(2)) // Thời gian tối đa thực hiện workflow
//        .setRetryOptions(RetryOptions.newBuilder()
//            .setInitialInterval(Duration.ofSeconds(2)) // chờ 2s trước khi thực hiện lần tiếp theo
//            .setMaximumAttempts(2) // Thử lại tối đa 3 lần nếu Activity lỗi mạng
//            .build())
//        .build();

    private final BookingTaskActivities activities = Workflow.newActivityStub(
        BookingTaskActivities.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .build())
            .build());

//    private final BookingTaskActivities activities = Workflow.newActivityStub(BookingTaskActivities.class, options);

//    @Override
//    public void updateBooking(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount, String email) {
//
//        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(false).build();
//        Saga saga = new Saga(sagaOptions);
//
//        try {
//            ConfirmBookingRequest confirmBookingRequest = new ConfirmBookingRequest();
//            confirmBookingRequest.setBookingId(bookingId);
//            confirmBookingRequest.setAmount(amount);
//            confirmBookingRequest.setPaymentId(paymentId);
//
//            BookingResponse bookingResponse = activities.updateBooking(confirmBookingRequest);
//
//            // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
//            // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity
//
//            UpdateSlotBlockRequest inventoryRequest = new UpdateSlotBlockRequest();
//            inventoryRequest.setSlotBlockId(slotBlockId);
//            inventoryRequest.setCustomerId(customerId);
//            inventoryRequest.setTourScheduleId(tourScheduleId);
//
//            UpdateSlotBlockResponse updateSlotBlockResponse = activities.updateInventory(inventoryRequest);
//            // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
//            // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity
//
//            EmailRequest emailRequest = new EmailRequest();
//            emailRequest.setToEmail(email);
//            emailRequest.setCustomerName(customerId);
//            emailRequest.setSubject("Đặt chỗ tour du lịch");
//            emailRequest.setBookingId(String.valueOf(bookingId));
//            emailRequest.setAccountId(bookingResponse.getAccountId());
//            emailRequest.setTourScheduleId(tourScheduleId);
//            emailRequest.setQuantity(bookingResponse.getQuantity());
//            emailRequest.setConfirmedSlots(updateSlotBlockResponse.getConfirmedSlots());
//            emailRequest.setTotalPrice(bookingResponse.getTotalPrice());
//            emailRequest.setPassengers(bookingResponse.getPassengers());
//            emailRequest.setOptionalServices(bookingResponse.getOptionalServices());
//            activities.notification(emailRequest);
//        }catch (Exception e) {
//            saga.compensate();
//            throw Workflow.wrap(e);
//        }
//     }

    // create
     @Override
     public void bookingTour(CreateBookingRequest request) {
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
                 .customerId(request.getAccountId().toString())
                 .amount(request.getQuantity())
                 .build();
             SlotBlockResponse slotResponse = activities.blockInventorySlot(slotReq);

             log.info("block slot success");

             log.info("payment");

             PaymentWebhookResponse paymentWebhookResponse = activities.payment(bookingResponse.getId());

             log.info("payment success");

             // Bắt đầu luồng update :
             //-----------------------------------------------------------------------------
             ConfirmBookingRequest confirmBookingRequest = new ConfirmBookingRequest();
             confirmBookingRequest.setBookingId(bookingResponse.getId());
             confirmBookingRequest.setAmount(bookingResponse.getTotalPrice());
             confirmBookingRequest.setPaymentId(paymentWebhookResponse.getPaymentId());

             BookingResponse bookingUpdateResponse = activities.updateBooking(confirmBookingRequest);

             // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
             // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity

             UpdateSlotBlockRequest inventoryRequest = new UpdateSlotBlockRequest();
             inventoryRequest.setSlotBlockId(slotResponse.getSlotBlockId());
             inventoryRequest.setCustomerId(bookingResponse.getAccountId().toString());
             inventoryRequest.setTourScheduleId(bookingResponse.getTourScheduleId());

             UpdateSlotBlockResponse updateSlotBlockResponse = activities.updateInventory(inventoryRequest);
             // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
             // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity

             EmailRequest emailRequest = new EmailRequest();
             emailRequest.setToEmail(request.getEmail());
             emailRequest.setCustomerName(bookingResponse.getAccountId().toString());
             emailRequest.setSubject("Đặt chỗ tour du lịch");
             emailRequest.setBookingId(String.valueOf(bookingResponse.getId()));
             emailRequest.setAccountId(bookingResponse.getAccountId());
             emailRequest.setTourScheduleId(bookingResponse.getTourScheduleId());
             emailRequest.setQuantity(bookingResponse.getQuantity());
             emailRequest.setConfirmedSlots(updateSlotBlockResponse.getConfirmedSlots());
             emailRequest.setTotalPrice(bookingResponse.getTotalPrice());
             emailRequest.setPassengers(bookingResponse.getPassengers());
             emailRequest.setOptionalServices(bookingResponse.getOptionalServices());
             activities.notification(emailRequest);

         } catch (TemporalFailure e) {
             saga.compensate();
             throw e;
         }
     }

}
