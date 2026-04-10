package com.vn.tech.tour_services.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.vn.tech.tour_services.dto.TourDetailResponse;
import com.vn.tech.tour_services.dto.TourSearchResponse;

public interface TourService {

    List<TourSearchResponse> searchTours(
        String q,
        String departures,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    );

    TourDetailResponse getTourDetail(String slug, UUID tourId);
}
