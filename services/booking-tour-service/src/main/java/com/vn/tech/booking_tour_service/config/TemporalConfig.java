//package com.vn.tech.booking_tour_service.config;
//
//import io.temporal.client.WorkflowClient;
//import io.temporal.serviceclient.WorkflowServiceStubs;
//import io.temporal.serviceclient.WorkflowServiceStubsOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import com.vn.tech.booking_tour_service.activity.impl.BookingTaskActivitiesImpl;
//import com.vn.tech.booking_tour_service.workflow.impl.BookingTaskWorkflowImpl;
//import io.temporal.worker.Worker;
//import io.temporal.worker.WorkerFactory;
//
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//
//@Configuration
//public class TemporalConfig {
//
//    @Value("${TEMPORAL_URL:127.0.0.1:7233}")
//    private String temporalServiceAddress;
//
//    // 1. Cấu hình kết nối tới Temporal Server
//    @Bean
//    @Primary
//    public WorkflowServiceStubs workflowServiceStubs() {
//        return WorkflowServiceStubs.newInstance(
//            WorkflowServiceStubsOptions.newBuilder()
//                .setTarget(temporalServiceAddress)
//                .build()
//        );
//    }
//
//    // 2. Cấu hình WorkflowClient (ĐÃ SỬA LỖI THIẾU NGOẶC)
//    @Bean
//    @Primary
//    public WorkflowClient workflowClient(WorkflowServiceStubs serviceStubs) {
//        return WorkflowClient.newInstance(serviceStubs);
//    }
//
//    // 3. Khởi tạo WorkerFactory và cấu hình Worker (KHÔNG START Ở ĐÂY)
//    @Bean
//    public WorkerFactory workerFactory(WorkflowClient workflowClient, BookingTaskActivitiesImpl bookingActivities) {
//        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
//
//        Worker worker = factory.newWorker("BookingTaskQueue");
//
//        worker.registerWorkflowImplementationTypes(BookingTaskWorkflowImpl.class);
//        worker.registerActivitiesImplementations(bookingActivities);
//
//        return factory;
//    }
//
//    // 4. CHỈ KHỞI ĐỘNG WORKER KHI SPRING BOOT ĐÃ HOÀN TOÀN SẴN SÀNG
//    @EventListener(ApplicationReadyEvent.class)
//    public void startWorkerFactory(WorkerFactory workerFactory) {
//        workerFactory.start();
//        System.out.println("=========================================================");
//        System.out.println("[TEMPORAL] ĐÃ KHỞI TẠO WORKER CHO BookingTaskQueue THÀNH CÔNG!");
//        System.out.println("=========================================================");
//    }
//}
