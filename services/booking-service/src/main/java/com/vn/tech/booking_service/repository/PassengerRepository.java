package com.vn.tech.booking_service.repository;

import com.vn.tech.booking_service.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity,UUID> {
    <Optional>List<PassengerEntity> findAllByBookingId(UUID bookingId);
}
