package com.vn.tech.booking_tour_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_booking_optional_services")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingOptionalServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID bookingId;
    private UUID optionalServiceId;
    private String serviceName;
    private int quantity;
    private String priceType;
    private Long unitPrice;
    private Long totalPrice;
}
