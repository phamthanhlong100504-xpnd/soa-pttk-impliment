package com.vn.tech.booking_tour_service.workflow.impl;

import com.vn.tech.booking_tour_service.activity.BookingTaskActivities;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.update.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import com.vn.tech.booking_tour_service.workflow.BookingTaskWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.UUID;

@WorkflowImpl(taskQueues = "BookingTaskQueue")
public class BookingTaskWorkflowImpl implements BookingTaskWorkflow {

    private final ActivityOptions options = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofMinutes(2)) // Thời gian tối đa thực hiện workflow
        .setRetryOptions(RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(2)) // chờ 2s trước khi thực hiện lần tiếp theo
            .setMaximumAttempts(2) // Thử lại tối đa 3 lần nếu Activity lỗi mạng
            .build())
        .build();

    private final BookingTaskActivities activities = Workflow.newActivityStub(BookingTaskActivities.class, options);

    @Override
    public void updateBooking(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount, String email) {

        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(false).build();
        Saga saga = new Saga(sagaOptions);

        try {
            ConfirmBookingRequest confirmBookingRequest = new ConfirmBookingRequest();
            confirmBookingRequest.setBookingId(bookingId);
            confirmBookingRequest.setAmount(amount);
            confirmBookingRequest.setPaymentId(paymentId);

            BookingResponse bookingResponse = activities.updateBooking(confirmBookingRequest);

            // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
            // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity

            UpdateSlotBlockRequest inventoryRequest = new UpdateSlotBlockRequest();
            inventoryRequest.setSlotBlockId(slotBlockId);
            inventoryRequest.setCustomerId(customerId);
            inventoryRequest.setTourScheduleId(tourScheduleId);

            UpdateSlotBlockResponse updateSlotBlockResponse = activities.updateInventory(inventoryRequest);
            // Đăng ký hành động hoàn tác (Rollback) nếu bước sau lỗi
            // saga.addCompensation(activities::cancelBooking, bookingId); // Bạn cần viết thêm hàm cancelBooking trong Activity

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setToEmail(email);
            emailRequest.setCustomerName(customerId);
            emailRequest.setSubject("Đặt chỗ tour du lịch");
            emailRequest.setBookingId(String.valueOf(bookingId));
            emailRequest.setAccountId(bookingResponse.getAccountId());
            emailRequest.setTourScheduleId(tourScheduleId);
            emailRequest.setQuantity(bookingResponse.getQuantity());
            emailRequest.setConfirmedSlots(updateSlotBlockResponse.getConfirmedSlots());
            emailRequest.setTotalPrice(bookingResponse.getTotalPrice());
            emailRequest.setPassengers(bookingResponse.getPassengers());
            emailRequest.setOptionalServices(bookingResponse.getOptionalServices());
            activities.notification(emailRequest);
        }catch (Exception e) {
            saga.compensate();
            throw Workflow.wrap(e);
        }
     }
}
