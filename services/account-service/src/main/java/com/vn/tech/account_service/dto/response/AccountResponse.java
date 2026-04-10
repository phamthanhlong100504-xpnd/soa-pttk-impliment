package com.vn.tech.account_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String role;
    private TokenResponse token;
}
