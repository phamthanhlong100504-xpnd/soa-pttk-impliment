package com.vn.tech.inventory_service.job;


import com.vn.tech.inventory_service.repository.SlotBlockRepository;
import com.vn.tech.inventory_service.service.TourInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlotBlockCleanupJob {

    private final SlotBlockRepository slotBlockRepository;
    private final TourInventoryService tourInventoryService;

    // Chạy mỗi 60,000 milliseconds (1 phút)
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredSlots() {
        log.info("[Inventory] [Cleanup Job] Bắt đầu quét các SlotBlocks hết hạn...");

        // 1. Tìm tất cả các ID hết hạn
        List<UUID> expiredIds = slotBlockRepository.findExpiredSlotBlockIds(LocalDateTime.now());

        if (expiredIds.isEmpty()) {
            return;
        }

        log.info("[inventory] [Cleanup Job] Tìm thấy {} bản ghi hết hạn cần xử lý.", expiredIds.size());

        // 2. Xử lý từng bản ghi một
        int successCount = 0;
        int errorCount = 0;

        for (UUID id : expiredIds) {
            try {
                // Gọi sang Service. Nhờ REQUIRES_NEW, nếu 1 thằng lỗi, thằng khác vẫn chạy bình thường
                tourInventoryService.releaseExpiredSlotBlock(id);
                successCount++;
            } catch (Exception e) {
                log.error("[Cleanup Job] Lỗi khi nhả vé cho SlotBlock ID: {}", id, e);
                errorCount++;
            }
        }

        log.info("[Cleanup Job] Hoàn tất. Thành công: {}, Thất bại: {}", successCount, errorCount);
    }
}
