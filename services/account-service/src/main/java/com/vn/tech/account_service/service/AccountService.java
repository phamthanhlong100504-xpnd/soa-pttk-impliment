package com.vn.tech.account_service.service;

import com.vn.tech.account_service.config.TokenHelper;
import com.vn.tech.account_service.dto.response.AccountResponse;
import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.exception.AppException;
import com.vn.tech.account_service.exception.ErrorCode;
import com.vn.tech.account_service.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountResponse getAccountInfo(String accessToken) {
        UUID id = TokenHelper.getAccountIdFromToken(accessToken);
        AccountEntity account =  accountRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return  AccountResponse.builder()
            .id(account.getId())
            .fullName(account.getFullName())
            .email(account.getEmail())
            .phoneNumber(account.getPhoneNumber())
            .dateOfBirth(account.getDateOfBirth())
            .build();
    }
}
