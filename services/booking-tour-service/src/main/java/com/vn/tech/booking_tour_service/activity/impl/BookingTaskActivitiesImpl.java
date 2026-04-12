package com.vn.tech.booking_tour_service.activity.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.tech.booking_tour_service.activity.BookingTaskActivities;
import com.vn.tech.booking_tour_service.client.BookingClient;
import com.vn.tech.booking_tour_service.client.InventoryClient;
import com.vn.tech.booking_tour_service.client.NotificationClient;
import com.vn.tech.booking_tour_service.common.PaymentStatus;
import com.vn.tech.booking_tour_service.dto.ApiResponseCreate;
import com.vn.tech.booking_tour_service.dto.InventoryApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.CreateBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.PaymentWebhookRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.SlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.request.initiate.ValidateTourScheduleRequest;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.GetAvailableSlotResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.PaymentWebhookResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import com.vn.tech.booking_tour_service.dto.response.update.ApiResponseUpdate;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import com.vn.tech.booking_tour_service.exception.AppException;
import com.vn.tech.booking_tour_service.exception.ErrorCode;
import feign.FeignException;
import io.temporal.spring.boot.ActivityImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@ActivityImpl
@AllArgsConstructor
@Slf4j
@Component
public class BookingTaskActivitiesImpl implements BookingTaskActivities {

    private final InventoryClient inventoryClient;

    private final BookingClient bookingClient;

    private  final NotificationClient notificationClient;


    private final ObjectMapper objectMapper;


    @Override
    public BookingResponse updateBooking(ConfirmBookingRequest confirmBookingRequest) {
        log.info("[Booking Tour Orchestrator] [Update] : Thực hiện cập nhật booking : {}",confirmBookingRequest.getBookingId());

        if (confirmBookingRequest.getBookingId() == null || confirmBookingRequest.getAmount() == null || confirmBookingRequest.getPaymentId() == null) {
            log.error("[Booking Tour Orchestrator]  [Update] Dữ liệu đầu vào không hợp lệ. BookingID: {}, PaymentID: {}, Amount: {}",
                confirmBookingRequest.getBookingId(),confirmBookingRequest.getAmount(),confirmBookingRequest.getPaymentId());
            throw new IllegalArgumentException("Saga Fail: Thiếu hoặc sai dữ liệu đầu vào để confirm booking.");
        }

//        ConfirmBookingRequest confirmBookingRequest = new ConfirmBookingRequest();
//
//        confirmBookingRequest.setBookingId(bookingId);
//        confirmBookingRequest.setAmount(amount);
//        confirmBookingRequest.setPaymentId(paymentId);

        BookingResponse dataBooking;

        ApiResponseCreate<BookingResponse> bookingResponseApiResponseUpdate;

        try {
            bookingResponseApiResponseUpdate = bookingClient.confirmBooking(confirmBookingRequest);
        } catch (FeignException e) {
            log.error("[Booking Tour Orchestrator] Lỗi mạng khi gọi Booking Service. Status: {}, Lỗi: {}", e.status(), e.getMessage());
            throw new RuntimeException("Saga Fail: Không thể kết nối hoặc Booking Service sập.");
        }

        if (bookingResponseApiResponseUpdate == null) {
            throw new RuntimeException("Saga Fail: Booking Service không trả về response.");
        }

        if (bookingResponseApiResponseUpdate.getCode() != 200) {
            log.error("[Booking Tour Orchestrator] Booking Service từ chối confirm. Lý do: {}", bookingResponseApiResponseUpdate.getMessage());
            throw new RuntimeException("Saga Fail: " + bookingResponseApiResponseUpdate.getMessage());
        }

        dataBooking = bookingResponseApiResponseUpdate.getResult();

        if (dataBooking == null) {
            throw new RuntimeException("Saga Fail: Booking Service báo thành công nhưng không trả về dữ liệu (Data rỗng).");
        }
        log.info("[Booking Tour Orchestrator] Đã chốt Booking {} thành công!",confirmBookingRequest.getBookingId());

        return dataBooking;
    }

    @Override
    public UpdateSlotBlockResponse updateInventory(UpdateSlotBlockRequest updateSlotBlockRequest) {
        UpdateSlotBlockResponse data = null;
        try {
            ResponseEntity<ApiResponseUpdate.Payload> response
                = inventoryClient.updateSlotsBlocks(updateSlotBlockRequest);

            if (response == null || response.getBody() == null) {
                throw new RuntimeException("Inventory Service không trả về dữ liệu (Response Body null).");
            }

            ApiResponseUpdate.Payload payload = response.getBody();

            log.info("Dữ liệu nhận được: code={}, message={}", payload.getCode(), payload.getMessage());

            if (payload.getCode() != 200) {
                String errorMsg = payload.getMessage();
                throw new RuntimeException("Inventory từ chối yêu cầu. Lý do: " + errorMsg);
            }

            if (payload.getData() == null) {
                throw new RuntimeException("Data rỗng, không có gì để map!");
            }

            // Chuyển đổi LinkedHashMap sang DTO cụ thể
            Object rawData = payload.getData();
            data = objectMapper.convertValue(rawData, UpdateSlotBlockResponse.class);

            log.info("Lấy được dữ liệu thành công; Booking-id : {}", data.getBookingId());

        } catch (FeignException e) {
            log.error("[Booking Tour Orchestrator] [Update] Gọi Inventory Service thất bại. Status: {}, Lỗi: {}", e.status(), e.getMessage());
            throw new RuntimeException("Saga Fail: Lỗi kết nối mạng tới Inventory.");

        } catch (IllegalArgumentException e) {
            log.error("[Booking Tour Orchestrator] [Update] Sai cấu trúc DTO từ Inventory: {}", e.getMessage());
            throw new RuntimeException("Saga Fail: Dữ liệu trả về từ Inventory không đúng định dạng.");

        } catch (Exception e) {
            log.error("[Booking Tour Orchestrator] [Update] Lỗi luồng Inventory: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return data;
    }

    @Override
    public void notification(EmailRequest request){
         notificationClient.sendEmail(request);
    }

    // create

    @Override
    public void validateTourSchedule(ValidateTourScheduleRequest validateTourScheduleRequest) {
        String tourScheduleId = validateTourScheduleRequest.getTourScheduleId().toString();
//        InventoryApiResponse inventoryApiResponse = inventoryClient.getAvailableSlot(tourScheduleId);

        ResponseEntity<InventoryApiResponse.Payload> response
            = inventoryClient.getAvailableSlot(tourScheduleId);

        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Inventory Service không trả về dữ liệu (Response Body null).");
        }

        InventoryApiResponse.Payload payload = response.getBody();

        if (payload.getCode() != 200) {
            String errorMsg = payload.getMessage();
            throw new RuntimeException("Inventory từ chối yêu cầu. Lý do: " + errorMsg);
        }

        if (payload.getData() == null) {
            throw new RuntimeException("Data rỗng, không có gì để map!");
        }

        Object rawData = payload.getData();

        GetAvailableSlotResponse data = objectMapper.convertValue(rawData, GetAvailableSlotResponse.class);

        int availableSlots = data.getAvailableSlots();

        if(availableSlots < validateTourScheduleRequest.getQuantity()) {
            throw new AppException(ErrorCode.TOUR_SCHEDULE_NOT_ENOUGH_SLOTS);
        }
    };

    @Override
    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        ApiResponseCreate response = bookingClient.createBooking(createBookingRequest);

        if (response == null) {
            throw new AppException(ErrorCode.CREATE_BOOKING_FAIL);
        }

        return (BookingResponse) response.getResult();

    };

    @Override
    public SlotBlockResponse blockInventorySlot(SlotBlockRequest slotBlockRequest) {

        ResponseEntity<InventoryApiResponse.Payload> response
            = inventoryClient.createSlotBlocks(slotBlockRequest);

        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Inventory Service không trả về dữ liệu (Response Body null).");
        }

        InventoryApiResponse.Payload payload = response.getBody();

        if (payload.getCode() != 200) {
            String errorMsg = payload.getMessage();
            throw new RuntimeException("Inventory từ chối yêu cầu. Lý do: " + errorMsg);
        }

        if (payload.getData() == null) {
            throw new RuntimeException("Data rỗng, không có gì để map!");
        }

        Object rawData = payload.getData();

        SlotBlockResponse data = objectMapper.convertValue(rawData, SlotBlockResponse.class);

        return data;
    };

//    @Override
//    public String generatePaymentUrl(GeneratePaymentUrlRequest generatePaymentUrlRequest) {
//        long orderCode = ThreadLocalRandom.current().nextLong(100000L, 1000000000L);
//
//        PaymentData paymentData = PaymentData.builder()
//            .orderCode(orderCode)
//            .amount(0)
//            .description("Thanh toan don: " + generatePaymentUrlRequest.getBookingId())
//            .returnUrl("test")
//            .cancelUrl("test")
//            .build();
//
//        try {
//            CheckoutResponseData response = payOS.createPaymentLink(paymentData);
//            return response.getCheckoutUrl();
//        } catch (Exception e) {
//            log.error("Lỗi tạo link thanh toán: {}", e.getMessage());
//            return null;
//        }
//    };

    @Override
    public PaymentWebhookResponse payment(UUID bookingId) {
        com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse booking = bookingClient.getBooking(bookingId);
        PaymentWebhookRequest paymentWebhookRequest = PaymentWebhookRequest.builder()
            .bookingId(bookingId)
            .paymentId(UUID.randomUUID().toString())
            .amount(booking.getTotalPrice())
            .paymentStatus(PaymentStatus.SUCCEEDED)
            .build();
        ApiResponseCreate<PaymentWebhookResponse> paymentWebhookResponse = bookingClient.handlePaymentWebhook(paymentWebhookRequest);

        return  (PaymentWebhookResponse) paymentWebhookResponse.getResult();
    }
}
