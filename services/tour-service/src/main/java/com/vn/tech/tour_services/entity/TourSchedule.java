package com.vn.tech.tour_services.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_schedules")
public class TourSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tour_id", nullable = false)
    private UUID tourId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "min_participants")
    private Integer minParticipants;

    @Column(length = 50)
    private String status;
}
