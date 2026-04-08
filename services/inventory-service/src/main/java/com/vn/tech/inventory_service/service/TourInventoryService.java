package com.vn.tech.inventory_service.service;

import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import org.springframework.stereotype.Service;

@Service
public interface TourInventoryService {
    public AvailableResponse getAvailableSlot(String tourScheduleId);
}
