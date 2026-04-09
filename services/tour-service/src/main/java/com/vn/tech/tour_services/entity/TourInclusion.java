package com.vn.tech.tour_services.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_inclusions")
public class TourInclusion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "tour_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private UUID tourId;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "LONGTEXT")
    private String description;
}
