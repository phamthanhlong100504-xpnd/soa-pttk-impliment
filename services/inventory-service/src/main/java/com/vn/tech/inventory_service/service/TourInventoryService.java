package com.vn.tech.inventory_service.service;

import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.dto.response.SlotBlockResponse;
import com.vn.tech.inventory_service.dto.response.UpdateSlotBlockResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TourInventoryService {
    public AvailableResponse getAvailableSlot(String tourScheduleId);

    public SlotBlockResponse createSlotBlock(UUID tourScheduleId,String customerId,Integer amount,String bookingId);

    public UpdateSlotBlockResponse updateSlotBlock(UUID tourScheduleId,String customerId);

    public void releaseExpiredSlotBlock(UUID slotBlockId);
}
