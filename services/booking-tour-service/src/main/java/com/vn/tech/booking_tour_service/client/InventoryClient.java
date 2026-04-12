package com.vn.tech.booking_tour_service.client;

import com.vn.tech.booking_tour_service.dto.request.update.UpdateSlotBlockRequest;
import com.vn.tech.booking_tour_service.dto.response.update.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", path = "api/v1/inventory")
public interface InventoryClient {

    @PutMapping("/slots-blocks")
    // SỬA DÒNG NÀY: Trả về thẳng ResponseEntity với Payload bên trong
    public ResponseEntity<ApiResponse.Payload> updateSlotsBlocks(@RequestBody UpdateSlotBlockRequest request);

}
