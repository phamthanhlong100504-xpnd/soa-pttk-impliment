package com.vn.tech.booking_tour_service.service;

import com.vn.tech.booking_tour_service.common.OutboxStatus;
import com.vn.tech.booking_tour_service.dto.request.booking.PaymentWebhookRequest;
import com.vn.tech.booking_tour_service.dto.response.booking.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.entity.BookingEntity;
import com.vn.tech.booking_tour_service.entity.OutboxEntity;
import com.vn.tech.booking_tour_service.entity.PaymentRecordEntity;
import com.vn.tech.booking_tour_service.exception.AppException;
import com.vn.tech.booking_tour_service.exception.ErrorCode;
import com.vn.tech.booking_tour_service.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private BookingRepository bookingRepository;
    private OutboxRepository outboxRepository;
    private PaymentRecordRepository paymentRecordRepository;
    private final ObjectMapper objectMapper;

    public PaymentWebhookResponse processPaymentWebhook(PaymentWebhookRequest paymentWebhookRequest) {
        log.info("Processing payment webhook: {}", paymentWebhookRequest.getIdempotencyKey());

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
            .amount(paymentWebhookRequest.getAmount())
            .status(paymentWebhookRequest.getPaymentStatus())
            .build();

        paymentRecordRepository.save(paymentRecord);

        JsonNode payload = objectMapper.valueToTree(paymentRecord);
        saveOutboxEvent("Payment",  paymentRecord.getId(), "PAYMENT_WEBHOOK_RECEIVED", payload);

        log.info("Payment webhook processed: {}", paymentRecord.getIdempotencyKey());

        return returnPaymentWebhookResponse(paymentRecord);
    }

    private void saveOutboxEvent(String aggregateType, UUID aggregateId, String eventType, JsonNode payload) {
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
