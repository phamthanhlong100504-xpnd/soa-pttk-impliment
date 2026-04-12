//package com.vn.tech.booking_tour_service.activity;
//
//import com.vn.tech.booking_tour_service.client.BookingClient;
//import com.vn.tech.booking_tour_service.client.InventoryClient;
//import com.vn.tech.booking_tour_service.common.PaymentStatus;
//import com.vn.tech.booking_tour_service.dto.ApiResponse;
//import com.vn.tech.booking_tour_service.dto.InventoryApiResponse;
//import com.vn.tech.booking_tour_service.dto.request.initiate.*;
//import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
//import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
//import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
//import com.vn.tech.booking_tour_service.exception.AppException;
//import com.vn.tech.booking_tour_service.exception.ErrorCode;
//import io.temporal.spring.boot.ActivityImpl;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import vn.payos.PayOS;
//import vn.payos.type.CheckoutResponseData;
//import vn.payos.type.PaymentData;
//
//import java.util.UUID;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Component
//@ActivityImpl
//@AllArgsConstructor
//@Slf4j
//public class BookingTaskActivitiesImpl implements BookingTaskActivities {
//    private final InventoryClient inventoryClient;
//    private final BookingClient bookingClient;
//
//    @Override
//    public void validateTourSchedule(ValidateTourScheduleRequest validateTourScheduleRequest) {
//        String tourScheduleId = validateTourScheduleRequest.getTourScheduleId().toString();
//        InventoryApiResponse inventoryApiResponse = inventoryClient.getAvailableSlot(tourScheduleId);
//
//        int availableSlots;
//
//        if (inventoryApiResponse.getBody() != null) {
//            availableSlots = (int) inventoryApiResponse.getBody().getData();
//        } else {
//            throw new AppException(ErrorCode.GET_TOUR_SCHEDULE_AVAILABLE_SLOT_FAIL);
//        }
//
//        if (availableSlots < validateTourScheduleRequest.getQuantity()) {
//            throw new AppException(ErrorCode.TOUR_SCHEDULE_NOT_ENOUGH_SLOTS);
//        }
//    };
//
//    @Override
//    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
//        ApiResponse response = bookingClient.createBooking(createBookingRequest);
//
//        if (response == null) {
//            throw new AppException(ErrorCode.CREATE_BOOKING_FAIL);
//        }
//
//        return (BookingResponse) response.getResult();
//
//    };
//
//    @Override
//    public SlotBlockResponse blockInventorySlot(SlotBlockRequest slotBlockRequest) {
//        InventoryApiResponse inventoryApiResponse = inventoryClient.createSlotBlocks(slotBlockRequest);
//        if (inventoryApiResponse.getBody() == null) {
//            throw new AppException(ErrorCode.CREATE_INVENTORY_BLOCK_SLOTS_FAIL);
//        }
//
//        if (inventoryApiResponse.getBody().getData() == null) {
//            throw new AppException(ErrorCode.CREATE_INVENTORY_BLOCK_SLOTS_FAIL);
//        }
//
//        return (SlotBlockResponse) inventoryApiResponse.getBody().getData();
//    };
//
////    @Override
////    public String generatePaymentUrl(GeneratePaymentUrlRequest generatePaymentUrlRequest) {
////        long orderCode = ThreadLocalRandom.current().nextLong(100000L, 1000000000L);
////
////        PaymentData paymentData = PaymentData.builder()
////            .orderCode(orderCode)
////            .amount(0)
////            .description("Thanh toan don: " + generatePaymentUrlRequest.getBookingId())
////            .returnUrl("test")
////            .cancelUrl("test")
////            .build();
////
////        try {
////            CheckoutResponseData response = payOS.createPaymentLink(paymentData);
////            return response.getCheckoutUrl();
////        } catch (Exception e) {
////            log.error("Lỗi tạo link thanh toán: {}", e.getMessage());
////            return null;
////        }
////    };
//
//    @Override
//    public PaymentWebhookResponse payment(UUID bookingId) {
//        BookingResponse booking = bookingClient.getBooking(bookingId);
//        PaymentWebhookRequest paymentWebhookRequest = PaymentWebhookRequest.builder()
//                .bookingId(bookingId)
//                .paymentId("Test thanh toan")
//                .amount(booking.getTotalPrice())
//                .paymentStatus(PaymentStatus.SUCCEEDED)
//                .build();
//        ApiResponse<PaymentWebhookResponse>  paymentWebhookResponse = bookingClient.handlePaymentWebhook(paymentWebhookRequest);
//
//        return  (PaymentWebhookResponse) paymentWebhookResponse.getResult();
//    }
//}
