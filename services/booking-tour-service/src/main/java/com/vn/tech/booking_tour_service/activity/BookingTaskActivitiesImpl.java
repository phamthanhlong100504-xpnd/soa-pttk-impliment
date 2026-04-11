package com.vn.tech.booking_tour_service.activity;

import com.vn.tech.booking_tour_service.client.BookingClient;
import com.vn.tech.booking_tour_service.client.InventoryClient;
import com.vn.tech.booking_tour_service.dto.ApiResponse;
import com.vn.tech.booking_tour_service.dto.InventoryApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.*;
import com.vn.tech.booking_tour_service.dto.response.initiate.BookingResponse;
import com.vn.tech.booking_tour_service.dto.response.initiate.SlotBlockResponse;
import com.vn.tech.booking_tour_service.exception.AppException;
import com.vn.tech.booking_tour_service.exception.ErrorCode;
import io.temporal.spring.boot.ActivityImpl;
import lombok.AllArgsConstructor;

@ActivityImpl
@AllArgsConstructor
public class BookingTaskActivitiesImpl implements BookingTaskActivities {
    private final InventoryClient inventoryClient;
    private final BookingClient bookingClient;

    @Override
    public void validateTourSchedule(ValidateTourSchedule validateTourSchedule) {
        String tourScheduleId = validateTourSchedule.getTourScheduleI().toString();
        InventoryApiResponse inventoryApiResponse = inventoryClient.getAvailableSlot(tourScheduleId);

        int availableSlots;

        if (inventoryApiResponse.getBody() != null) {
            availableSlots = (int) inventoryApiResponse.getBody().getData();
        } else {
            throw new AppException(ErrorCode.GET_TOUR_SCHEDULE_AVAILABLE_SLOT_FAIL);
        }

        if (availableSlots < validateTourSchedule.getQuantity()) {
            throw new AppException(ErrorCode.TOUR_SCHEDULE_NOT_ENOUGH_SLOTS);
        }
    };

    @Override
    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        ApiResponse response = bookingClient.createBooking(createBookingRequest);

        if (response == null) {
            throw new AppException(ErrorCode.CREATE_BOOKING_FAIL);
        }

        return (BookingResponse) response.getResult();

    };

    @Override
    public SlotBlockResponse blockInventorySlot(SlotBlockRequest slotBlockRequest) {
        InventoryApiResponse inventoryApiResponse = inventoryClient.createSlotBlocks(slotBlockRequest);
        if (inventoryApiResponse.getBody() == null) {
            throw new AppException(ErrorCode.CREATE_INVENTORY_BLOCK_SLOTS_FAIL);
        }

        if (inventoryApiResponse.getBody().getData() == null) {
            throw new AppException(ErrorCode.CREATE_INVENTORY_BLOCK_SLOTS_FAIL);
        }

        return (SlotBlockResponse) inventoryApiResponse.getBody().getData();
    };

    @Override
    public void generatePaymentUrl(GeneratePaymentUrlRequest generatePaymentUrlRequest) {

    };
}
