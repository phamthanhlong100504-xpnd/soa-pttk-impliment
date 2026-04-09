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
@Table(name = "tour_reviews")
public class TourReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "tour_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private UUID tourId;

    @Column(name = "customer_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private UUID customerId;

    @Column(name = "booking_id", columnDefinition = "VARCHAR(36)", unique = true)
    private UUID bookingId;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 50)
    private String status;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(columnDefinition = "LONGTEXT")
    private String comment;
}
