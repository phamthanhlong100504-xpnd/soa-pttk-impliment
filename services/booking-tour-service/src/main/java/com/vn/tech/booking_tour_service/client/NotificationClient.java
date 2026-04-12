package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/api/v1/notifications/booking-tickets")
public interface NotificationClient {

    @PostMapping("/send")
    void sendEmail(@RequestBody EmailRequest request);
}
