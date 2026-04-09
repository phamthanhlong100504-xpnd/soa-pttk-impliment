package com.vn.tech.account_service.controller;

import com.vn.tech.account_service.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Object> health() {
        return ApiResponse.builder()
            .code(200)
            .message("Account service hoạt động")
            .build();
    }
}


