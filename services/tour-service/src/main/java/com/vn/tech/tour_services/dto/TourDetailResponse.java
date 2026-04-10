package com.vn.tech.tour_services.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailResponse {

    private TourResponse tour;

    @Builder.Default
    @JsonProperty("tour_itinerary")
    private List<TourItineraryResponse> tourItinerary = new ArrayList<>();
}
