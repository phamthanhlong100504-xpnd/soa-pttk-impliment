package com.vn.tech.inventory_service.service.impl;

import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.model.Inventory;
import com.vn.tech.inventory_service.model.TourSchedule;
import com.vn.tech.inventory_service.repository.InventoryRepository;
import com.vn.tech.inventory_service.repository.TourScheduleRepository;
import com.vn.tech.inventory_service.service.TourInventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TourInventoryServiceImpl implements TourInventoryService {

    private final TourScheduleRepository tourScheduleRepository;

    private final InventoryRepository inventoryRepository;

    public TourInventoryServiceImpl(TourScheduleRepository tourScheduleRepository, InventoryRepository inventoryRepository) {
        this.tourScheduleRepository = tourScheduleRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public AvailableResponse getAvailableSlot(String tourScheduleId) {
        AvailableResponse availableResponse = new AvailableResponse();
        UUID uuid = UUID.fromString(tourScheduleId);


        Inventory inventory = getInventoryByScheduleId(uuid);
        log.info("[TourInventoryServiceImpl] Nhận được các dữ liệu; totalSlots : {}; availableSlots : {}; blockSlots : {}; confirmmed Slots : {}",
            inventory.getTotalSlots(),inventory.getAvailableSlots(),inventory.getBlockedSlots(),inventory.getConfirmedSlots());

        int availableSlots = inventory.getTotalSlots() - inventory.getBlockedSlots() - inventory.getConfirmedSlots();

        availableResponse.setAvailableSlots(availableSlots);

        return availableResponse;
    }


    public Inventory getInventoryByScheduleId(UUID scheduleId) {
        return tourScheduleRepository.findById(scheduleId)
            .map(tourSchedule -> tourSchedule.getInventory())
            .orElseThrow(() -> new EntityNotFoundException("[TourInventoryServiceImpl] Không tìm thấy kho cho lịch trình này"));
    }

}


