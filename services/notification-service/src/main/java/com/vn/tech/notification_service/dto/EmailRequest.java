package com.vn.tech.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; // Nếu bạn dùng Lombok thì chỉ cần @Data là khỏi cần viết Get/Set
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmailRequest {
    private String toEmail;
    private String customerName;
    private String subject;

    // Dữ liệu chi tiết của Tour
    private String bookingId;
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Integer confirmedSlots;
    private Long totalPrice;

    private List<PassengerResponse> passengers;
    private List<BookingOptionalServiceResponse> optionalServices;

    @Data
    public static class PassengerResponse {
        private String fullName;
    }

    @Data
    public static class BookingOptionalServiceResponse {
        private String serviceName;
        private int quantity;
        private String priceType;
    }
}
