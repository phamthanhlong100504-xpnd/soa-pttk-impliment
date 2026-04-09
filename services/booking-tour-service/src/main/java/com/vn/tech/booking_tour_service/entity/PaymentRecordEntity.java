package com.vn.tech.booking_tour_service.entity;

import com.vn.tech.booking_tour_service.common.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
