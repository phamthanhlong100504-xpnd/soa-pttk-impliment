package com.vn.tech.tour_services.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vn.tech.tour_services.entity.TourItinerary;

public interface TourItineraryRepository extends JpaRepository<TourItinerary, UUID> {

    @Query(value = """
        SELECT ti.*
        FROM tour_itinerary ti
        JOIN tours t ON t.id = ti.tour_id
        WHERE t.slug = :slug
        ORDER BY ti.day_number ASC
        """, nativeQuery = true)
    List<TourItinerary> findByTourSlugOrderByDayNumberAsc(@Param("slug") String slug);
}
