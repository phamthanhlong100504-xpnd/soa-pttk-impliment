package com.vn.tech.tour_services.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourCategoryId implements Serializable {

    private UUID tourId;
    private UUID categoryId;
}
