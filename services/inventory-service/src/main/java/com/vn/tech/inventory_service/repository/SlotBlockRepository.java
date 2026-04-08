package com.vn.tech.inventory_service.repository;


import com.vn.tech.inventory_service.model.SlotBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SlotBlockRepository extends JpaRepository<SlotBlock, UUID> {

}
