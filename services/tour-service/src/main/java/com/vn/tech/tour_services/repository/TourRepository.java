package com.vn.tech.tour_services.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vn.tech.tour_services.entity.Tour;

public interface TourRepository extends JpaRepository<Tour, UUID> {

	@Query(value = """
		SELECT DISTINCT t.*
		FROM tours t
		LEFT JOIN departures dp ON dp.id = t.departure_id
		WHERE (:q IS NULL
		       OR LOWER(t.slug) LIKE CONCAT('%', :q, '%'))
		  AND UPPER(t.status) = 'ACTIVE'
		  AND (:departures IS NULL OR LOWER(dp.name) = LOWER(:departures))
		  AND (:startDate IS NULL OR EXISTS (
		      SELECT 1
		      FROM tour_schedules ts
		      WHERE ts.tour_id = t.id
		        AND ts.start_date = :startDate
		  ))
		  AND (:durationDays IS NULL OR t.duration_days = :durationDays)
		  AND (:minPrice IS NULL OR t.base_price >= :minPrice)
		  AND (:maxPrice IS NULL OR t.base_price <= :maxPrice)
		""", nativeQuery = true)
	List<Tour> searchTours(
		@Param("q") String q,
		@Param("departures") String departures,
		@Param("startDate") LocalDate startDate,
		@Param("durationDays") Integer durationDays,
		@Param("minPrice") BigDecimal minPrice,
		@Param("maxPrice") BigDecimal maxPrice
	);

}
