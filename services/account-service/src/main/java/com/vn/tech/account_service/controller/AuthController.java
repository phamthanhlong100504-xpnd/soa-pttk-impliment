package com.vn.tech.account_service.controller;

import com.vn.tech.account_service.dto.ApiResponse;
import com.vn.tech.account_service.dto.request.LoginRequest;
import com.vn.tech.account_service.dto.request.RefreshRequest;
import com.vn.tech.account_service.dto.request.SignUpRequest;
import com.vn.tech.account_service.dto.response.AccountResponse;
import com.vn.tech.account_service.dto.response.TokenResponse;
import com.vn.tech.account_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/log-in")
    public ApiResponse<AccountResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return ApiResponse.<AccountResponse>builder()
            .code(200)
            .message("Đăng nhập thành công")
            .result(authService.login(loginRequest))
            .build();
    }

    @PostMapping("/sign-up")
    public ApiResponse<AccountResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return ApiResponse.<AccountResponse>builder()
            .code(200)
            .message("Đăng kí thành công")
            .result(authService.signUp(signUpRequest))
            .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ApiResponse.<TokenResponse>builder()
            .code(200)
            .message("Cấp token mới thành công")
            .result(authService.refreshToken(refreshRequest))
            .build();
    }
}
