package com.vn.tech.tour_services.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.tech.tour_services.entity.TourSchedule;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, UUID> {

    List<TourSchedule> findByTourIdOrderByStartDateAsc(UUID tourId);
}
