package com.vn.tech.booking_tour_service.dto.response.update;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {

    private UUID id;
    //    private String bookingCode;
    private UUID accountId;
    private UUID tourScheduleId;
    private String tourName;
    private int quantity;
    private Long totalPrice;

//    @Enumerated(EnumType.STRING)
//    @Builder.Default
//    private BookingStatus bookingStatus = BookingStatus.PENDING_PAYMENT;
//
//    private String paymentId;
//
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
    private List<PassengerResponse> passengers;

    private List<BookingOptionalServiceResponse> optionalServices;

}
