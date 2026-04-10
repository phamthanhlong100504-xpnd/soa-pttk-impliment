package com.vn.tech.booking_service.entity;

import com.vn.tech.booking_service.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "tbl_payment_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID bookingId;
    private String paymentId;
    private String idempotencyKey;
    private Long amount;
    private PaymentStatus status;
}
