package com.vn.tech.inventory_service.client;

import com.vn.tech.inventory_service.dto.request.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/api/v1/notifications/booking-tickets")
public interface NotificationClient {

    @PostMapping("/send")
    String sendEmail(@RequestBody EmailRequest request);
}
