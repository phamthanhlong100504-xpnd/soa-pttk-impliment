package com.vn.tech.tour_services.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.tech.tour_services.entity.TourItinerary;

public interface TourItineraryRepository extends JpaRepository<TourItinerary, UUID> {

    List<TourItinerary> findByTourIdOrderByDayNumberAsc(UUID tourId);
}
