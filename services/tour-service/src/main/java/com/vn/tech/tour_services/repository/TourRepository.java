package com.vn.tech.tour_services.repository;

import com.vn.tech.tour_services.entity.Tour;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, UUID> {
}
