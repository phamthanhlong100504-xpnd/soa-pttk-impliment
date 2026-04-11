package com.vn.tech.booking_service.dto.response.booking;

import com.vn.tech.booking_service.common.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingResponse {

    private UUID id;
    private String bookingCode;
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingStatus bookingStatus = BookingStatus.PENDING_PAYMENT;

    private String paymentId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private List<PassengerResponse> passengers;

    private List<BookingOptionalServiceResponse> optionalServices;

}
