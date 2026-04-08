package com.vn.tech.inventory_service.service;

import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.dto.response.SlotBlockResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TourInventoryService {
    public AvailableResponse getAvailableSlot(String tourScheduleId);

    public SlotBlockResponse createSlotBlock(UUID tourScheduleId,String customerId,Integer amount,String bookingId);
}
