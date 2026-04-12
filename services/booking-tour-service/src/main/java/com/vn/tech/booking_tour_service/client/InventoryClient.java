package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.InventoryApiResponse;
import com.vn.tech.booking_tour_service.dto.request.initiate.SlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.update.ApiResponseUpdate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("api/v1/inventory/{tour-schedule-id}")
    public ResponseEntity<InventoryApiResponse.Payload> getAvailableSlot(@PathVariable("tour-schedule-id") String tourScheduleId);

    @PostMapping("api/v1/inventory/slot-blocks")
    public ResponseEntity<InventoryApiResponse.Payload> createSlotBlocks(@RequestBody SlotBlockRequest request);

    @PutMapping("api/v1/inventory/slots-blocks")
    public ResponseEntity<ApiResponseUpdate.Payload> updateSlotsBlocks(@RequestBody UpdateSlotBlockRequest request);

}
