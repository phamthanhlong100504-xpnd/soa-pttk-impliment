package com.vn.tech.account_service.controller;

import com.vn.tech.account_service.config.TokenHelper;
import com.vn.tech.account_service.dto.ApiResponse;
import com.vn.tech.account_service.dto.response.AccountResponse;
import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private AccountService accountService;

    @GetMapping("{accountId}")
    public ApiResponse<AccountResponse> getAccountInfo(@PathVariable("accountId") UUID accountId) {
        return ApiResponse.<AccountResponse>builder()
            .code(200)
            .message("Lấy thông tin tài khoản thành công")
            .result(accountService.getAccountInfo(accountId))
            .build();
    }
}
