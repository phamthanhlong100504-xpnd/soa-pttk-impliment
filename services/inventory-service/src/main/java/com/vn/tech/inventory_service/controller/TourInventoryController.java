package com.vn.tech.inventory_service.controller;

import com.vn.tech.inventory_service.dto.request.AvailableRequest;
import com.vn.tech.inventory_service.dto.response.ApiResponse;
import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.dto.response.SuccessResponse;
import com.vn.tech.inventory_service.service.TourInventoryService;
import lombok.RequiredArgsConstructor;
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
}
