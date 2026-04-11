package com.vn.tech.account_service.service;

import com.vn.tech.account_service.config.TokenHelper;
import com.vn.tech.account_service.dto.request.LoginRequest;
import com.vn.tech.account_service.dto.request.RefreshRequest;
import com.vn.tech.account_service.dto.request.SignUpRequest;
import com.vn.tech.account_service.dto.response.AccountResponse;
import com.vn.tech.account_service.dto.response.TokenResponse;
import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.exception.AppException;
import com.vn.tech.account_service.exception.ErrorCode;
import com.vn.tech.account_service.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private AccountRepository accountRepository;
    private TokenHelper tokenHelper;

    @Transactional
    public AccountResponse signUp(SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(accountRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        signUpRequest.setPassword(BCrypt.hashpw(signUpRequest.getPassword(), BCrypt.gensalt()));
        AccountEntity account = AccountEntity.builder()
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .password(signUpRequest.getPassword())
            .fullName(signUpRequest.getFullName())
            .phoneNumber(signUpRequest.getPhoneNumber())
            .dateOfBirth(signUpRequest.getDateOfBirth())
            .role("USER")
            .build();

        accountRepository.save(account);

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(tokenHelper.generateAccessToken(account))
            .refreshToken(tokenHelper.generateRefreshToken(account))
            .build();

        return AccountResponse.builder()
            .id(account.getId())
            .fullName(account.getFullName())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .dateOfBirth(account.getDateOfBirth())
            .role(account.getRole())
            .token(tokenResponse)
            .build();
    }

    @Transactional
    public AccountResponse login(LoginRequest loginRequest) {
        AccountEntity account = accountRepository.findByUsername(loginRequest.getUsername());
        if (Objects.isNull(account)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if (!BCrypt.checkpw(loginRequest.getPassword(), account.getPassword())) {
            throw new AppException(ErrorCode.INCORRECT_PASSWORD);
        }

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(tokenHelper.generateAccessToken(account))
            .refreshToken(tokenHelper.generateRefreshToken(account))
            .build();

        return AccountResponse.builder()
            .id(account.getId())
            .email(account.getEmail())
            .fullName(account.getFullName())
            .token(tokenResponse)
            .build();
    }

    @Transactional
    public TokenResponse refreshToken(RefreshRequest refreshRequest) {
        if (Boolean.FALSE.equals(tokenHelper.validateRefreshToken(refreshRequest.getRefreshToken()))) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        UUID userId = tokenHelper.getAccountIdFromToken("Bearer " + refreshRequest.getRefreshToken());

        AccountEntity userEntity = accountRepository.findById(userId).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return TokenResponse.builder()
            .accessToken(tokenHelper.generateAccessToken(userEntity))
            .refreshToken(tokenHelper.generateRefreshToken(userEntity))
            .build();

    }
}
