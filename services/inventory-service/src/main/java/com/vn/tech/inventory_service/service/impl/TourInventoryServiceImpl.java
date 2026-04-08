package com.vn.tech.inventory_service.service.impl;

import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.dto.response.SlotBlockResponse;
import com.vn.tech.inventory_service.model.Inventory;
import com.vn.tech.inventory_service.model.InventoryHistory;
import com.vn.tech.inventory_service.model.SlotBlock;
import com.vn.tech.inventory_service.model.TourSchedule;
import com.vn.tech.inventory_service.repository.InventoryHistoryRepository;
import com.vn.tech.inventory_service.repository.InventoryRepository;
import com.vn.tech.inventory_service.repository.SlotBlockRepository;
import com.vn.tech.inventory_service.repository.TourScheduleRepository;
import com.vn.tech.inventory_service.service.TourInventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor // thay thế khởi tạo bằng contructor
public class TourInventoryServiceImpl implements TourInventoryService {

    private int expireTime = 5;

    private final TourScheduleRepository tourScheduleRepository;

    private final InventoryRepository inventoryRepository;

    private final SlotBlockRepository slotBlockRepository;

    private final InventoryHistoryRepository inventoryHistoryRepository;


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


    // Đánh dấu @Transaction để cập nhật các bước khi có lỗi còn rollback
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SlotBlockResponse createSlotBlock(UUID tourScheduleId, String customerId, Integer amount, String bookingId) {

        // Kiểm tra xem tồn tại tourScheduleId chưa
        Inventory inventory = inventoryRepository.findById(tourScheduleId)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy kho vé cho Lịch trình: " + tourScheduleId));

        if (inventory.getAvailableSlots() < amount) {
            throw new IllegalStateException("Không đủ số lượng vé trống. Vui lòng chọn số lượng ít hơn.");
        }

        // số lượng trước khi đặt
        Integer previousAvailableSlots = inventory.getTotalSlots() - inventory.getBlockedSlots() - inventory.getConfirmedSlots();

        inventory.setBlockedSlots(inventory.getBlockedSlots() + amount);
        inventory.setAvailableSlots(previousAvailableSlots - amount);
        inventoryRepository.save(inventory);

        // Tạo SlotBlocks
        SlotBlock slotBlock = createSlotBlockEntity(tourScheduleId,customerId,amount,bookingId);
        slotBlockRepository.save(slotBlock);


        // số lượng giảm đi
        Integer  newAvailableSlots = previousAvailableSlots - amount;

        // Ghi vào Inventory_history
        InventoryHistory inventoryHistory = createInventoryHistory(customerId,tourScheduleId,amount,newAvailableSlots,previousAvailableSlots);
        inventoryHistoryRepository.save(inventoryHistory);

        return SlotBlockResponse.builder()
            .actionType("PENDING")
            .newAvailableSlots(newAvailableSlots)
            .previousAvailableSlots(previousAvailableSlots)
            .amount(amount)
            .build();
    }

    public SlotBlock createSlotBlockEntity(UUID tourScheduleId, String customerId, Integer amount, String bookingId) {
        TourSchedule tourScheduleProxy = tourScheduleRepository.getReferenceById(tourScheduleId);
        return SlotBlock.builder()
            .tourSchedule(tourScheduleProxy)
            .customerId(UUID.fromString(customerId))
            .quantity(amount)
            .status(SlotBlock.SlotBlockStatus.PENDING) // Đánh dấu trạng thái là PENDING
            .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
            .bookingId(UUID.fromString(bookingId))
            .build();
    }

    public InventoryHistory createInventoryHistory(String customerId,UUID tourScheduleId,Integer amount,Integer  newAvailableSlots ,Integer previousAvailableSlots) {

        Inventory inventoryProxy = inventoryRepository.getReferenceById(tourScheduleId);

        return InventoryHistory.builder()
            .createdAt(Instant.now())
            .actor(customerId)
            .note("")
            .actionType(InventoryHistory.ActionType.BLOCK)
            .inventory(inventoryProxy)
            .quantityChanged(amount)
            .previousAvailableSlots(previousAvailableSlots)
            .newAvailableSlots(newAvailableSlots)
            .build();
    }

    public Inventory getInventoryByScheduleId(UUID scheduleId) {
        return tourScheduleRepository.findById(scheduleId)
            .map(tourSchedule -> tourSchedule.getInventory())
            .orElseThrow(() -> new EntityNotFoundException("[TourInventoryServiceImpl] Không tìm thấy kho cho lịch trình này"));
    }

}


