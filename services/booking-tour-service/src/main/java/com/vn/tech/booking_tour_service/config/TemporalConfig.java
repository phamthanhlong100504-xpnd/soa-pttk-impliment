package com.vn.tech.booking_tour_service.config;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.vn.tech.booking_tour_service.activity.impl.BookingTaskActivitiesImpl;
import com.vn.tech.booking_tour_service.workflow.impl.BookingTaskWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    @Value("${TEMPORAL_URL:127.0.0.1:7233}")
    private String temporalServiceAddress;

    @Bean
    @Primary
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newInstance(
            WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalServiceAddress)
                .build());
    }

    @Bean
    @Primary
    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
        return WorkflowClient.newInstance(serviceStubs);
    // Spring Boot sẽ tự động truyền WorkflowClient và ActivityImpl của bạn vào đây
    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient, BookingTaskActivitiesImpl bookingActivities) {

        // 1. Tạo Factory từ Client
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);

        // 2. Tạo Worker và gắn vào Queue của bạn (Tên phải khớp YAML)
        Worker worker = factory.newWorker("BookingTaskQueue");

        // 3. Đăng ký Workflow và Activity (BẮT BUỘC)
        worker.registerWorkflowImplementationTypes(BookingTaskWorkflowImpl.class);
        worker.registerActivitiesImplementations(bookingActivities);

        // 4. ÉP KHỞI ĐỘNG WORKER
        factory.start();

        System.out.println("=========================================================");
        System.out.println("[TEMPORAL] ĐÃ KHỞI TẠO WORKER CHO BookingTaskQueue THÀNH CÔNG!");
        System.out.println("=========================================================");

        return factory;
    }
}
