package com.vn.tech.inventory_service.repository;


import com.vn.tech.inventory_service.model.SlotBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotBlockRepository extends JpaRepository<SlotBlock, UUID> {
    // Spring sẽ tự dịch hàm này thành:
    // SELECT * FROM slot_blocks WHERE tour_schedule_id = ? AND customer_id = ? AND status = ?
    Optional<SlotBlock> findByTourScheduleIdAndCustomerIdAndStatus(
        UUID tourScheduleId,
        UUID customerId,
        SlotBlock.SlotBlockStatus status
    );

    // Tìm các slot đang PENDING và có expiresAt nhỏ hơn thời gian hiện tại
    @Query("SELECT s.id FROM SlotBlock s WHERE s.status = 'PENDING' AND s.expiresAt < :currentTime")
    List<UUID> findExpiredSlotBlockIds(@Param("currentTime") LocalDateTime currentTime);

}
