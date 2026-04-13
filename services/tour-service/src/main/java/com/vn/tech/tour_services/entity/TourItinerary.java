package com.vn.tech.tour_services.entity;

import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_itinerary")
public class TourItinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tour_id", nullable = false)
    private UUID tourId;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT") // Đổi từ LONGTEXT sang TEXT
    private String description;

    @JdbcTypeCode(SqlTypes.JSON) // Hibernate 6 mapping JSON cho Postgres
    @Column(columnDefinition = "jsonb")
    private String meals;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String activities;
}
