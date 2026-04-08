package com.vn.tech.inventory_service.controller;

import com.vn.tech.inventory_service.dto.request.SlotBlockRequest;
import com.vn.tech.inventory_service.dto.request.UpdateSlotBlockRequest;
import com.vn.tech.inventory_service.dto.response.*;
import com.vn.tech.inventory_service.service.TourInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/inventory")
public class TourInventoryController {

    private final TourInventoryService tourInventoryService;

    public TourInventoryController(TourInventoryService tourInventoryService) {
        this.tourInventoryService = tourInventoryService;
    }

    @PostMapping("/test")
    public String test() {
        return "Ready";
    }

    @GetMapping("/{tour-schedule-id}")
    public ApiResponse getAvailableSlot(@PathVariable("tour-schedule-id") String tourScheduleId){
        log.info("[Inventory Service] [TourInventoryController] Thực hiện lấy ra số slots còn trống của tour_scheduleId : {}",tourScheduleId);

        AvailableResponse response = tourInventoryService.getAvailableSlot(tourScheduleId);

        return new SuccessResponse(response,"Success! Lấy ra số lượng còn trống");
    }

    @PostMapping("/slot-blocks")
    public ApiResponse createSlotBlocks(@RequestBody SlotBlockRequest request) {
        log.info("[Inventory Service] [TourInventoryController] Thực hiện tạo slot-blocks giúp giữ chỗ; tourScheduleId : {}, customerId : {}, amount : {}, bookingId : {}",
            request.getTourScheduleId(),request.getCustomerId(),request.getAmount(),request.getBookingId());

        SlotBlockResponse response = tourInventoryService.createSlotBlock(request.getTourScheduleId(),request.getCustomerId(),request.getAmount(),request.getBookingId());

        return new SuccessResponse(response, "Success! Tạo slot-blocks thành công");

    }

    @PutMapping("/slots-blocks")
    public ApiResponse updateSlotsBlocks(@RequestBody UpdateSlotBlockRequest request) {
        log.info("[Inventory Service] [TourInventoryController] Thực hiện cập nhật slot-blocks giúp giữ chỗ; tourScheduleId : {}, customerId : {}",
            request.getTourScheduleId(),request.getCustomerId());

        UpdateSlotBlockResponse response = tourInventoryService.updateSlotBlock(request.getTourScheduleId(),request.getCustomerId());

        return new SuccessResponse(response, "Success! Cập nhật slot-blocks thành công");

    }



}
