package com.vn.tech.tour_services.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourItineraryResponse {

    private UUID id;

    @JsonProperty("tour_id")
    private UUID tourId;

    @JsonProperty("day_number")
    private Integer dayNumber;

    private String title;
    private String description;
    private String meals;
    private String activities;
}
