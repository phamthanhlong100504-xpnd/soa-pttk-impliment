package com.vn.tech.tour_services.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_optional_services")
public class TourOptionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "tour_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private UUID tourId;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "pricing_type")
    private String pricingType;

    private String category;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    @Column(name = "min_quantity")
    private Integer minQuantity;
}
