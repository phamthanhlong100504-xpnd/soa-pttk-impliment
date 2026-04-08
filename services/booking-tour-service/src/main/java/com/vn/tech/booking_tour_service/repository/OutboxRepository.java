package com.vn.tech.booking_tour_service.repository;

import com.vn.tech.booking_tour_service.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
}
