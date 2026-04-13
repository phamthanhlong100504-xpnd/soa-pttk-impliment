package com.vn.tech.inventory_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmailRequest {
    private String email;
    private String name;
    private String subject;
    private String message;
}
