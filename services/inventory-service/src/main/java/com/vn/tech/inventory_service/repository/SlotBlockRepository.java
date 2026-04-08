package com.vn.tech.inventory_service.repository;


import com.vn.tech.inventory_service.model.SlotBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
