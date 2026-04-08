package com.vn.tech.booking_tour_service.repository;

import com.vn.tech.booking_tour_service.entity.BookingOptionalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingOptionalServiceRepository extends JpaRepository<BookingOptionalServiceEntity, UUID> {
    List<BookingOptionalServiceEntity> findAllByBookingId(UUID bookingId);
}
