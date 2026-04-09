package com.vn.tech.account_service.controller;

import com.vn.tech.account_service.config.TokenHelper;
import com.vn.tech.account_service.dto.ApiResponse;
import com.vn.tech.account_service.dto.response.AccountResponse;
import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private AccountService accountService;

    @GetMapping
    public ApiResponse<AccountResponse> getAccountInfo(@RequestHeader("Authorization") String accessToken) {
        return ApiResponse.<AccountResponse>builder()
            .code(200)
            .message("Lấy thông tin tài khoản thành công")
            .result(accountService.getAccountInfo(accessToken))
            .build();
    }
}
