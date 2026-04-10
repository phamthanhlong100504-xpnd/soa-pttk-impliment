package com.vn.tech.booking_service.entity;

import com.vn.tech.booking_service.common.BookingStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    private String bookingCode;
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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
