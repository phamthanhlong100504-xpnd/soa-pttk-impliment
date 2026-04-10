package com.vn.tech.booking_service.controller;

import com.vn.tech.booking_service.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Object> health() {
        return ApiResponse.builder()
            .code(200)
            .message("Booking service hoạt động")
            .build();
    }
}

