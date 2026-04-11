package com.vn.tech.booking_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.tech.booking_service.common.OutboxStatus;
import com.vn.tech.booking_service.dto.request.booking.PaymentWebhookRequest;
import com.vn.tech.booking_service.dto.response.booking.PaymentWebhookResponse;
import com.vn.tech.booking_service.entity.BookingEntity;
import com.vn.tech.booking_service.entity.OutboxEntity;
import com.vn.tech.booking_service.entity.PaymentRecordEntity;
import com.vn.tech.booking_service.exception.AppException;
import com.vn.tech.booking_service.exception.ErrorCode;
import com.vn.tech.booking_service.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private BookingRepository bookingRepository;
    private OutboxRepository outboxRepository;
    private PaymentRecordRepository paymentRecordRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public PaymentWebhookResponse processPaymentWebhook(PaymentWebhookRequest paymentWebhookRequest) {
        log.info("[controller] --> [service] Processing payment webhook: {}", paymentWebhookRequest.getIdempotencyKey());

        PaymentRecordEntity existingPayment = paymentRecordRepository.findByIdempotencyKey(paymentWebhookRequest.getIdempotencyKey());
        if (existingPayment != null) {
            log.info("Payment webhook already processed: {}", paymentWebhookRequest.getIdempotencyKey());
            return returnPaymentWebhookResponse(existingPayment);
        }

        BookingEntity booking = bookingRepository.findById(paymentWebhookRequest.getBookingId())
            .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        PaymentRecordEntity paymentRecord = PaymentRecordEntity.builder()
            .paymentId(paymentWebhookRequest.getPaymentId())
            .bookingId(paymentWebhookRequest.getBookingId())
            .idempotencyKey(paymentWebhookRequest.getIdempotencyKey())
            .amount(paymentWebhookRequest.getAmount())
            .status(paymentWebhookRequest.getPaymentStatus())
            .build();

        paymentRecordRepository.save(paymentRecord);

        Map<String, Object> payload = objectMapper.convertValue(paymentRecord, Map.class);
        saveOutboxEvent("Payment",  paymentRecord.getId(), "PAYMENT_WEBHOOK_RECEIVED", payload);

        log.info("[controller] --> [service] Payment webhook processed: {}", paymentRecord.getIdempotencyKey());

        return returnPaymentWebhookResponse(paymentRecord);
    }

    private void saveOutboxEvent(String aggregateType, UUID aggregateId, String eventType, Map<String, Object> payload) {
        OutboxEntity outbox = OutboxEntity.builder()
            .aggregateType(aggregateType)
            .aggregateId(aggregateId)
            .eventType(eventType)
            .payload(payload)
            .status(OutboxStatus.PENDING)
            .build();

        outboxRepository.save(outbox);
    }

    public PaymentWebhookResponse returnPaymentWebhookResponse(PaymentRecordEntity paymentRecord) {
        return PaymentWebhookResponse.builder()
            .paymentId(paymentRecord.getPaymentId())
            .bookingId(paymentRecord.getBookingId())
            .amount(paymentRecord.getAmount())
            .status(paymentRecord.getStatus())
            .build();

    }
}
