//package com.vn.tech.booking_tour_service.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.vn.tech.booking_tour_service.client.BookingClient;
//import com.vn.tech.booking_tour_service.client.InventoryClient;
//import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
//import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
//import com.vn.tech.booking_tour_service.dto.response.create.ApiResponse;
//import com.vn.tech.booking_tour_service.dto.response.update.BookingResponse;
//import com.vn.tech.booking_tour_service.dto.response.update.SuccessResponse;
//import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
//import com.vn.tech.booking_tour_service.service.UpdateBookingTourService;
//import feign.FeignException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UpdateBookingTourServiceImpl implements UpdateBookingTourService {
//
//    private final InventoryClient inventoryClient;
//
//    private final BookingClient bookingClient;
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void updateBookingTour(UUID tourScheduleId, String customerId, UUID slotBlockId, UUID bookingId, String paymentId, Long amount) {
//        log.info("[Booking Tour Orchestrator] [Update] : Thực hiện cập nhật booking : {}",bookingId);
//
//        if (bookingId == null || paymentId == null || amount == null || amount <= 0) {
//            log.error("[Booking Tour Orchestrator]  [Update] Dữ liệu đầu vào không hợp lệ. BookingID: {}, PaymentID: {}, Amount: {}", bookingId, paymentId, amount);
//            throw new IllegalArgumentException("Saga Fail: Thiếu hoặc sai dữ liệu đầu vào để confirm booking.");
//        }
//
//        ConfirmBookingRequest confirmBookingRequest = new ConfirmBookingRequest();
//
//        confirmBookingRequest.setBookingId(bookingId);
//        confirmBookingRequest.setAmount(amount);
//        confirmBookingRequest.setPaymentId(paymentId);
//
//        ApiResponse<BookingResponse> bookingResponseApiResponse;
//        BookingResponse dataBooking;
//
//        try {
//            bookingResponseApiResponse = bookingClient.confirmBooking(confirmBookingRequest);
//        } catch (FeignException e) {
//            log.error("[Booking Tour Orchestrator] Lỗi mạng khi gọi Booking Service. Status: {}, Lỗi: {}", e.status(), e.getMessage());
//            throw new RuntimeException("Saga Fail: Không thể kết nối hoặc Booking Service sập.");
//        }
//
//        if (bookingResponseApiResponse == null) {
//            throw new RuntimeException("Saga Fail: Booking Service không trả về response.");
//        }
//
//        if (bookingResponseApiResponse.getCode() != 200) {
//            log.error("[Booking Tour Orchestrator] Booking Service từ chối confirm. Lý do: {}", bookingResponseApiResponse.getMessage());
//            throw new RuntimeException("Saga Fail: " + bookingResponseApiResponse.getMessage());
//        }
//
//        dataBooking = bookingResponseApiResponse.getResult();
//
//        if (dataBooking == null) {
//            throw new RuntimeException("Saga Fail: Booking Service báo thành công nhưng không trả về dữ liệu (Data rỗng).");
//        }
//        log.info("[Booking Tour Orchestrator] Đã chốt Booking {} thành công!", bookingId);
//
//
//
//
//    }
//}
