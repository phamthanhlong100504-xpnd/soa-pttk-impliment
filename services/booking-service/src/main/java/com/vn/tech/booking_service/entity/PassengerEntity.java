package com.vn.tech.booking_service.entity;

import com.vn.tech.booking_service.common.PassengerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tbl_passengers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID bookingId;
    private String fullName;
    private LocalDate dateOfBirth;
    private PassengerType  passengerType;
}
