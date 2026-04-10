package com.vn.tech.notification_service.dto;

import lombok.Data; // Nếu bạn dùng Lombok thì chỉ cần @Data là khỏi cần viết Get/Set

@Data
public class EmailRequest {
    private String email;
    private String name;
    private String subject;
    private String message;
    // Thêm 2 trường này để nhét vào HTML
    private String bookingId;
    private Integer amount;
}
