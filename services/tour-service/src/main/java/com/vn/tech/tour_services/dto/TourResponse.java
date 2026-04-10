package com.vn.tech.tour_services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {

    private UUID id;
    private String slug;
    private String name;
    private String description;
    private UUID destinationId;
    private UUID departureId;
    private BigDecimal basePrice;
    private Integer durationDays;
    private Integer durationNights;
    private String status;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    @JsonProperty("tour_schedules")
    private List<TourScheduleResponse> tourSchedules = new ArrayList<>();
}
