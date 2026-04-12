package com.vn.tech.tour_services.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tours")
@EntityListeners(AuditingEntityListener.class) // Thêm để tự động điền created_at/updated_at
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT") // Đổi từ LONGTEXT sang TEXT
    private String description;

    @Column(name = "destination_id")
    private UUID destinationId;

    @Column(name = "departure_id")
    private UUID departureId;

    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "duration_nights")
    private Integer durationNights;

    @Column(length = 50)
    private String status;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
