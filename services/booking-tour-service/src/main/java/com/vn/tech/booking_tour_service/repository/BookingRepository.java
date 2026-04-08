package com.vn.tech.booking_tour_service.repository;

import com.vn.tech.booking_tour_service.entity.BookingEntity;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
}
