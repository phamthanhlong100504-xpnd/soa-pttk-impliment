package com.university.document_service.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;



@Data
public class BookingTicketRequest {
    private String bookingId; // Nên giữ lại làm mã vé
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Integer confirmedSlots;
    private Long totalPrice;

    // Thêm 2 List chứa Object
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
