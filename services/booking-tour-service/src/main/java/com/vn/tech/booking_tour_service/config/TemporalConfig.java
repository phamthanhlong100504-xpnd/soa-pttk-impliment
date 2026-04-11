package com.vn.tech.booking_tour_service.config;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {
    @Bean
    public WorkflowClient workflowClient() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        return WorkflowClient.newInstance(service);
    }
}
