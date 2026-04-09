package com.vn.tech.booking_service.repository;

import com.vn.tech.booking_service.entity.PaymentRecordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRecordRepository extends CrudRepository<PaymentRecordEntity, UUID> {
    PaymentRecordEntity findByPaymentIdAndIdempotencyKey(String paymentId, String idempotencyKey);
    PaymentRecordEntity findByIdempotencyKey(String idempotencyKey);
}
