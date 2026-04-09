package com.vn.tech.tour_services.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "tour_categories")
@IdClass(TourCategoryId.class)
public class TourCategory {

    @Id
    @Column(name = "tour_id", columnDefinition = "VARCHAR(36)")
    private UUID tourId;

    @Id
    @Column(name = "category_id", columnDefinition = "VARCHAR(36)")
    private UUID categoryId;
}
