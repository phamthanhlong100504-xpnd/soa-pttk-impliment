package com.vn.tech.booking_tour_service.dto.request.booking;

import com.vn.tech.booking_tour_service.common.PassengerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PassengerRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private PassengerType passengerType;
}
