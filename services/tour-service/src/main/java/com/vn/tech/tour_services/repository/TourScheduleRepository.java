package com.vn.tech.tour_services.repository;

import com.vn.tech.tour_services.entity.TourSchedule;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, UUID> {

    @Query("select ts.tourId from TourSchedule ts where ts.startDate = :startDate")
    List<UUID> findTourIdsByStartDate(@Param("startDate") LocalDate startDate);
}
