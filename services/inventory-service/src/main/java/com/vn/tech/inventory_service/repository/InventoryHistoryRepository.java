package com.vn.tech.inventory_service.repository;

import com.vn.tech.inventory_service.model.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, UUID> {

}
