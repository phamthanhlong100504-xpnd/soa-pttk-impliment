package com.vn.tech.inventory_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vn.tech.inventory_service.dto.response.AvailableResponse;
import com.vn.tech.inventory_service.dto.response.SlotBlockResponse;
import com.vn.tech.inventory_service.dto.response.UpdateSlotBlockResponse;
import com.vn.tech.inventory_service.model.*;
import com.vn.tech.inventory_service.repository.*;
import com.vn.tech.inventory_service.service.TourInventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    private final ObjectMapper objectMapper;

    private final OutboxRepository outboxRepository;


    @Override
    public AvailableResponse getAvailableSlot(String tourScheduleId) {
        AvailableResponse availableResponse = new AvailableResponse();
        UUID uuid = UUID.fromString(tourScheduleId);

        Inventory inventory = getInventoryByScheduleId(uuid);

        log.info("[Inventory] [TourInventoryServiceImpl] Nhận được các dữ liệu; totalSlots : {}; availableSlots : {}; blockSlots : {}; confirmmed Slots : {}",
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

        log.info("[Inventory] [TourInventoryServiceImpl] Cập nhật thành công inventory với số lượng chỗ hiện tại : {}",previousAvailableSlots - amount);

        // Tạo SlotBlocks
        SlotBlock slotBlock = createSlotBlockEntity(tourScheduleId,customerId,amount,bookingId);
        SlotBlock saveSlotBlock = slotBlockRepository.save(slotBlock);

        log.info("[Inventory] [TourInventoryServiceImpl] Tạo một slotblock thành công");

        // số lượng giảm đi
        Integer  newAvailableSlots = previousAvailableSlots - amount;

        // Ghi vào Inventory_history
        InventoryHistory inventoryHistory = createInventoryHistory(customerId,tourScheduleId,amount,newAvailableSlots,previousAvailableSlots);
        inventoryHistoryRepository.save(inventoryHistory);

        log.info("[Inventory] [TourInventoryServiceImpl] Cập nhật history thành công");

        return SlotBlockResponse.builder()
            .actionType("PENDING")
            .newAvailableSlots(newAvailableSlots)
            .previousAvailableSlots(previousAvailableSlots)
            .slotBlockId(saveSlotBlock.getId())
            .amount(amount)
            .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateSlotBlockResponse updateSlotBlock(UUID tourScheduleId, String customerId , UUID slotBlockId) {

        // Lọc theo customerId, tourScheduleId và status
//        SlotBlock slotBlock = slotBlockRepository.findByTourScheduleIdAndCustomerIdAndStatus(
//            tourScheduleId,
//            UUID.fromString(customerId),
//            SlotBlock.SlotBlockStatus.PENDING
//        ).orElseThrow(() -> new IllegalArgumentException("Giữ chỗ đã hết hạn hoặc không tồn tại đối với khách hàng này."));

        SlotBlock slotBlock = slotBlockRepository.findById(slotBlockId)
            .orElseThrow(() -> new IllegalArgumentException("Giữ chỗ đã hết hạn hoặc không tồn tại đối với khách hàng này."));

        // kiểm tra tính lũy đẳng

        if(SlotBlock.SlotBlockStatus.CONFIRMED.equals(slotBlock.getStatus())) {
            log.info("[Inventory] [Idempotency] Slot block {} đã được confirm từ trước. Bỏ qua xử lý DB để tránh trừ đúp vé.", slotBlockId);
            return UpdateSlotBlockResponse.builder()
                .actionType("ALREADY_CONFIRMED")
                .confirmedSlots(slotBlock.getQuantity())
//                .bookingId(slotBlock.getBookingId().toString())
                .bookingId(slotBlock.getBookingId().toString())
                .build();
        }

        Integer amount = slotBlock.getQuantity();

        slotBlock.setStatus(SlotBlock.SlotBlockStatus.CONFIRMED);
        slotBlockRepository.save(slotBlock);
        log.info("[Inventory] [TourInventoryServiceImpl] Tạo một slotblock thành công!");

        // cập nhật tồn kho
        Inventory inventory = inventoryRepository.findById(tourScheduleId)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy kho vé cho Lịch trình: " + tourScheduleId));

        inventory.setBlockedSlots(inventory.getBlockedSlots() - amount);
        inventory.setConfirmedSlots(inventory.getConfirmedSlots() + amount);

        inventoryRepository.save(inventory);
        log.info("[Inventory] [TourInventoryServiceImpl] Cập nhật thành công inventory thành công");

        Integer currentAvailable = inventory.getTotalSlots() - inventory.getBlockedSlots() - inventory.getConfirmedSlots();

        InventoryHistory history = createConfirmInventoryHistory(
            customerId,
            tourScheduleId,
            amount,
            currentAvailable,
            slotBlock.getBookingId().toString()
        );
        history.setCreatedAt(Instant.now());
        inventoryHistoryRepository.save(history);

        log.info("[Inventory] [TourInventoryServiceImpl] Cập nhật history thành công!");

        // ==========================================
        // 4. LƯU VÀO BẢNG OUTBOX (BÁO CÁO KẾT QUẢ CHO SAGA)
        // ==========================================
        try {
            // Tạo payload dạng JSON để gửi đi
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("bookingId", slotBlock.getBookingId().toString());
            payload.put("tourScheduleId", tourScheduleId.toString());
            payload.put("amount", amount);

            OutboxEntity outbox = OutboxEntity.builder()
                .aggregateId(slotBlock.getBookingId().toString()) // Gắn với BookingID để thằng Nhạc trưởng dễ xử lý
                .aggregateType("INVENTORY")
                .eventType("INVENTORY_UPDATED_SUCCESS") // Tên topic hoặc Header để Kafka/Debezium nhận diện
                .payload(payload.toString()) // Lưu chuỗi JSON
                .status("PENDING") // Trạng thái chờ Debezium/Job quét
                .createdAt(Instant.now())
                .build();

            outboxRepository.save(outbox);

            log.info("[Inventory] Lưu sự kiện vào Outbox thành công. EventType: INVENTORY_UPDATED_SUCCESS");
        } catch (Exception e) {
            log.error("[Inventory] Lỗi khi lưu Outbox. Buộc Rollback toàn bộ giao dịch!", e);
            throw e; // Ném lỗi để @Transactional rollback lại cả kho vé
        }

        return UpdateSlotBlockResponse.builder()
            .actionType("CONFIRMED")
            .confirmedSlots(amount)
            .bookingId(slotBlock.getBookingId().toString())
            .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void releaseExpiredSlotBlock(UUID slotBlockId) {

        // 1. Lấy và kiểm tra lại chắc chắn nó vẫn đang PENDING
        SlotBlock slotBlock = slotBlockRepository.findById(slotBlockId)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy SlotBlock"));

        if (slotBlock.getStatus() != SlotBlock.SlotBlockStatus.PENDING) {
            return; // Đã được xử lý bởi luồng khác, bỏ qua
        }

        Integer amount = slotBlock.getQuantity();
        UUID tourScheduleId = slotBlock.getTourSchedule().getId();

        slotBlock.setStatus(SlotBlock.SlotBlockStatus.EXPIRED);
        slotBlockRepository.save(slotBlock);

        // 3. Hoàn trả tồn kho (INVENTORY)
        Inventory inventory = inventoryRepository.findById(tourScheduleId)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Inventory"));

        Integer previousAvailableSlots = inventory.getTotalSlots() - inventory.getConfirmedSlots() - inventory.getBlockedSlots();

        // Logic cốt lõi: Trừ đi blocked, Trả lại available
        inventory.setBlockedSlots(inventory.getBlockedSlots() - amount);
        inventory.setAvailableSlots(previousAvailableSlots +  amount);
        inventoryRepository.save(inventory);

        // 4. Ghi lịch sử (INVENTORY HISTORY)
        Integer newAvailableSlots = inventory.getAvailableSlots();
        InventoryHistory history = createInventoryHistory(
            "SYSTEM_JOB", // Actor là hệ thống tự chạy
            tourScheduleId,
            amount,
            newAvailableSlots,
            previousAvailableSlots

        );
        history.setActionType(InventoryHistory.ActionType.EXPIRE); // Thêm Enum RELEASE
        history.setNote("Hệ thống tự động nhả vé do quá hạn giữ chỗ");
        history.setReferenceId(slotBlock.getBookingId());

        inventoryHistoryRepository.save(history);

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
            .note("Khách hàng đã đặt trước vé thành công! Chờ thanh toán")
            .actionType(InventoryHistory.ActionType.BLOCK)
            .inventory(inventoryProxy)
            .quantityChanged(amount)
            .previousAvailableSlots(previousAvailableSlots)
            .newAvailableSlots(newAvailableSlots)
            .build();
    }


    public InventoryHistory createConfirmInventoryHistory(String customerId, UUID tourScheduleId, Integer amount, Integer currentAvailableSlots, String bookingId) {

        // Dùng Proxy để tăng tốc độ, không bắn lệnh SELECT
        Inventory inventoryProxy = inventoryRepository.getReferenceById(tourScheduleId);

        return InventoryHistory.builder()
            .inventory(inventoryProxy)
            .actionType(InventoryHistory.ActionType.CONFIRM) // Cần khai báo thêm CONFIRM trong Enum của bạn
            .quantityChanged(amount)
            .previousAvailableSlots(currentAvailableSlots)
            .newAvailableSlots(currentAvailableSlots) // Không thay đổi
            .actor(customerId)
            .referenceId(UUID.fromString(bookingId)) // Ghi lại mã Booking để tra cứu chéo
            .note("Khách hàng đã thanh toán và chốt vé thành công")
            .build();
    }

    public Inventory getInventoryByScheduleId(UUID scheduleId) {
        return tourScheduleRepository.findById(scheduleId)
            .map(tourSchedule -> tourSchedule.getInventory())
            .orElseThrow(() -> new EntityNotFoundException("[TourInventoryServiceImpl] Không tìm thấy kho cho lịch trình này"));
    }

}


