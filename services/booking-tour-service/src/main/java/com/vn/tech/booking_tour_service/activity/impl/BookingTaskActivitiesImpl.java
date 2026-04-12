package com.vn.tech.booking_tour_service.activity.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.tech.booking_tour_service.activity.BookingTaskActivities;
import com.vn.tech.booking_tour_service.client.BookingClient;
import com.vn.tech.booking_tour_service.client.InventoryClient;
import com.vn.tech.booking_tour_service.client.NotificationClient;
import com.vn.tech.booking_tour_service.dto.request.update.ConfirmBookingRequest;
import com.vn.tech.booking_tour_service.dto.request.update.EmailRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.create.ApiResponse;
import com.vn.tech.booking_tour_service.dto.response.update.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.update.UpdateSlotBlockResponse;
import feign.FeignException;
import io.temporal.spring.boot.ActivityImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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

        ApiResponse<BookingResponse> bookingResponseApiResponse;
        BookingResponse dataBooking;

        try {
            bookingResponseApiResponse = bookingClient.confirmBooking(confirmBookingRequest);
        } catch (FeignException e) {
            log.error("[Booking Tour Orchestrator] Lỗi mạng khi gọi Booking Service. Status: {}, Lỗi: {}", e.status(), e.getMessage());
            throw new RuntimeException("Saga Fail: Không thể kết nối hoặc Booking Service sập.");
        }

        if (bookingResponseApiResponse == null) {
            throw new RuntimeException("Saga Fail: Booking Service không trả về response.");
        }

        if (bookingResponseApiResponse.getCode() != 200) {
            log.error("[Booking Tour Orchestrator] Booking Service từ chối confirm. Lý do: {}", bookingResponseApiResponse.getMessage());
            throw new RuntimeException("Saga Fail: " + bookingResponseApiResponse.getMessage());
        }

        dataBooking = bookingResponseApiResponse.getResult();

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
            ResponseEntity<com.vn.tech.booking_tour_service.dto.response.update.ApiResponse.Payload> response
                = inventoryClient.updateSlotsBlocks(updateSlotBlockRequest);

            if (response == null || response.getBody() == null) {
                throw new RuntimeException("Inventory Service không trả về dữ liệu (Response Body null).");
            }

            com.vn.tech.booking_tour_service.dto.response.update.ApiResponse.Payload payload = response.getBody();

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
}
