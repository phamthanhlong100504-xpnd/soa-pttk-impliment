package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.InventoryApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.SlotBlockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("api/v1/inventory/{tour-schedule-id}")
    public InventoryApiResponse getAvailableSlot(@PathVariable("tour-schedule-id") String tourScheduleId);

    @PostMapping("api/v1/inventory/slot-blocks")
    public InventoryApiResponse createSlotBlocks(@RequestBody SlotBlockRequest request);
}
