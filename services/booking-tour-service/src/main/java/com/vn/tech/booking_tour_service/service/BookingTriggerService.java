package com.vn.tech.booking_tour_service.service;

import com.vn.tech.booking_tour_service.dto.response.update.WorkflowResponse;
import com.vn.tech.booking_tour_service.dto.response.update.WorkflowStatusResponse;
import com.vn.tech.booking_tour_service.workflow.BookingTaskWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingTriggerService {

    private final WorkflowClient workflowClient;
    private final WorkflowServiceStubs workflowServiceStubs;

    // update
    public WorkflowResponse triggerUpdateBookingWorkflow(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount, String email) {

        String workflowId = "BookingUpdate" + bookingId.toString();

        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue("BookingTaskQueue")
            .setWorkflowId("BookingUpdate-" + bookingId.toString()) // Đặt ID duy nhất để tránh chạy trùng lặp (Idempotency)
            .build();

        BookingTaskWorkflow workflow = workflowClient.newWorkflowStub(BookingTaskWorkflow.class, options);

        WorkflowClient.start(() -> workflow.updateBooking(
            tourScheduleId,
            customerId,
            slotBlockId,
            bookingId,
            paymentId,
            amount,
            email
        ));

        System.out.println("[Booking Tour Orchestrator] [Update] : Đã đẩy lệnh xử lý Booking vào Temporal!");
        return new WorkflowResponse(workflowId,"PROCESSING","Yêu cầu đã được tiếp nhận và đang xử lý");
    }

    // get status
    public WorkflowStatusResponse getWorkflowStatus (String bookingId) {
        String workflowId = "BookingUpdate-" + bookingId;

        DescribeWorkflowExecutionRequest request = DescribeWorkflowExecutionRequest.newBuilder()
            .setNamespace("default")
            .setExecution(WorkflowExecution.newBuilder().setWorkflowId(workflowId))
            .build();

        try {
            DescribeWorkflowExecutionResponse response = workflowServiceStubs.blockingStub().describeWorkflowExecution(request);
            String status = response.getWorkflowExecutionInfo().getStatus().toString();

            return new WorkflowStatusResponse(workflowId, status, "");
        }catch (Exception e) {
            return new WorkflowStatusResponse(workflowId, "NOT_FOUND", "Không tìm thấy luồng xử lý.");
        }
    }
}
