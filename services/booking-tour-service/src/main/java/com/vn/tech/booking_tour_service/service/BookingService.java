package com.vn.tech.booking_tour_service.service;

import com.vn.tech.booking_tour_service.common.BookingStatus;
import com.vn.tech.booking_tour_service.common.OutboxStatus;
import com.vn.tech.booking_tour_service.common.PaymentStatus;
import com.vn.tech.booking_tour_service.dto.request.booking.*;
import com.vn.tech.booking_tour_service.dto.response.booking.BookingOptionalServiceResponse;
import com.vn.tech.booking_tour_service.dto.response.booking.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.booking.PassengerResponse;
import com.vn.tech.booking_tour_service.dto.response.booking.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.entity.*;
import com.vn.tech.booking_tour_service.exception.AppException;
import com.vn.tech.booking_tour_service.exception.ErrorCode;
import com.vn.tech.booking_tour_service.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BookingService {
    private BookingRepository  bookingRepository;
    private PassengerRepository passengerRepository;
    private BookingOptionalServiceRepository bookingOptionalServiceRepository;
    private OutboxRepository outboxRepository;
    private PaymentRecordRepository paymentRecordRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        log.info("create booking for account: {}, tour schedule {}",
            createBookingRequest.getAccountId(), createBookingRequest.getTourScheduleId());

        BookingEntity booking = BookingEntity.builder()
            .tourScheduleId(createBookingRequest.getTourScheduleId())
            .accountId(createBookingRequest.getAccountId())
            .tourName(createBookingRequest.getTourName())
            .quantity(createBookingRequest.getQuantity())
            .totalPrice(createBookingRequest.getTotalPrice())
            .build();

        booking = bookingRepository.save(booking);

        List<PassengerRequest> passengerRequests = createBookingRequest.getPassengerRequests();
        for (PassengerRequest passengerRequest : passengerRequests) {
            PassengerEntity passenger = PassengerEntity.builder()
                .bookingId(booking.getId())
                .fullName(passengerRequest.getFullName())
                .dateOfBirth(passengerRequest.getDateOfBirth())
                .passengerType(passengerRequest.getPassengerType())
                .build();

            passengerRepository.save(passenger);
        }

        if (createBookingRequest.getBookingOptionalServiceRequests() != null) {
            List<BookingOptionalServiceRequest> bookingOptionalServiceRequests = createBookingRequest.getBookingOptionalServiceRequests();
            for (BookingOptionalServiceRequest bookingOptionalServiceRequest : bookingOptionalServiceRequests) {
                BookingOptionalServiceEntity bookingOptionalService = BookingOptionalServiceEntity.builder()
                    .bookingId(booking.getId())
                    .optionalServiceId(bookingOptionalServiceRequest.getOptionalServiceId())
                    .serviceName(bookingOptionalServiceRequest.getServiceName())
                    .priceType(bookingOptionalServiceRequest.getPriceType())
                    .quantity(bookingOptionalServiceRequest.getQuantity())
                    .unitPrice(bookingOptionalServiceRequest.getUnitPrice())
                    .totalPrice(bookingOptionalServiceRequest.getQuantity() * bookingOptionalServiceRequest.getUnitPrice())
                    .build();

                bookingOptionalServiceRepository.save(bookingOptionalService);
            }
        }

        JsonNode payload = objectMapper.valueToTree(booking);

        saveOutboxEvent("Booking", booking.getId(), "CREATED_BOOKING", payload);

        log.info("Create booking {} successfully", booking.getId());

        return returnBookingResponse(booking);
    }

    @Transactional
    public BookingResponse confirmBooking(ConfirmBookingRequest confirmBookingRequest) {
        log.info("confirmed booking: {}", confirmBookingRequest.getBookingId());

        BookingEntity booking = bookingRepository.findById(confirmBookingRequest.getBookingId())
            .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXIST));

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        JsonNode payload = objectMapper.valueToTree(booking);

        saveOutboxEvent("Booking", booking.getId(), "CONFIRMED_BOOKING", payload);

        log.info("Confirm booking {} successfully", booking.getId());

        return returnBookingResponse(booking);
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

    public BookingResponse returnBookingResponse(BookingEntity booking) {
        List<PassengerEntity> passengers = passengerRepository.findAllByBookingId(booking.getId());
        List<PassengerResponse>  passengerResponseList = new ArrayList<>();
        for  (PassengerEntity passenger : passengers) {
            PassengerResponse passengerResponse = PassengerResponse.builder()
                .id(passenger.getId())
                .fullName(passenger.getFullName())
                .dateOfBirth(passenger.getDateOfBirth())
                .passengerType(passenger.getPassengerType())
                .build();

            passengerResponseList.add(passengerResponse);
        }

        List<BookingOptionalServiceResponse> bookingOptionalServiceResponseList = new ArrayList<>();

        List<BookingOptionalServiceEntity> optionalServices =
            bookingOptionalServiceRepository.findAllByBookingId(booking.getId());

        for (BookingOptionalServiceEntity optionalService : optionalServices) {
            BookingOptionalServiceResponse optionalServiceResponse =
                BookingOptionalServiceResponse.builder()
                    .id(optionalService.getId())
                    .optionalServiceId(optionalService.getOptionalServiceId())
                    .serviceName(optionalService.getServiceName())
                    .unitPrice(optionalService.getUnitPrice())
                    .quantity(optionalService.getQuantity())
                    .priceType(optionalService.getPriceType())
                    .totalPrice(optionalService.getTotalPrice())
                    .build();

            bookingOptionalServiceResponseList.add(optionalServiceResponse);
        }

        BookingResponse bookingResponse = BookingResponse.builder()
            .id(booking.getId())
            .accountId(booking.getAccountId())
            .tourScheduleId(booking.getTourScheduleId())
            .tourName(booking.getTourName())
            .quantity(booking.getQuantity())
            .totalPrice(booking.getTotalPrice())
            .passengers(passengerResponseList)
            .optionalServices(bookingOptionalServiceResponseList)
            .build();

        return bookingResponse;
    }

}
