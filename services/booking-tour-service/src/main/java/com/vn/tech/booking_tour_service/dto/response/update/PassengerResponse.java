package com.vn.tech.booking_tour_service.dto.response.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vn.tech.booking_tour_service.common.PassengerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassengerResponse {
    private UUID id;
    private String fullName;
    private LocalDate dateOfBirth;
    private PassengerType passengerType;
}
