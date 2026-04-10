package com.vn.tech.tour_services.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vn.tech.tour_services.entity.TourSchedule;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, UUID> {

    @Query("select ts.tourId from TourSchedule ts where ts.startDate = :startDate")
    List<UUID> findTourIdsByStartDate(@Param("startDate") LocalDate startDate);

    List<TourSchedule> findByTourIdIn(List<UUID> tourIds);

    List<TourSchedule> findByTourIdOrderByStartDateAsc(UUID tourId);

    @Query(value = """
        SELECT ts.*
        FROM tour_schedules ts
        JOIN tours t ON t.id = ts.tour_id
        WHERE t.slug = :slug
        ORDER BY ts.start_date ASC
        """, nativeQuery = true)
    List<TourSchedule> findByTourSlugOrderByStartDateAsc(@Param("slug") String slug);
}
