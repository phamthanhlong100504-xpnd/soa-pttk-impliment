package com.vn.tech.booking_tour_service.config;

import com.vn.tech.booking_tour_service.activity.impl.BookingTaskActivitiesImpl;
import com.vn.tech.booking_tour_service.workflow.impl.BookingTaskWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TemporalWorkerConfig {
    private WorkflowClient workflowClient;
    private BookingTaskActivitiesImpl bookingTaskActivitiesImpl;

    @PostConstruct
    public void startWorker() {
        // 1. Khởi tạo Factory
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);

        // 2. Tạo Worker lắng nghe trên một Task Queue cố định
        Worker worker = factory.newWorker("BOOKING_TASK_QUEUE");

        // 3. Đăng ký Workflow Implementation
        worker.registerWorkflowImplementationTypes(BookingTaskWorkflowImpl.class);

        // 4. Đăng ký Activity Implementation
        worker.registerActivitiesImplementations(bookingTaskActivitiesImpl);

        // 5. Bắt đầu lắng nghe
        factory.start();
    }
}
