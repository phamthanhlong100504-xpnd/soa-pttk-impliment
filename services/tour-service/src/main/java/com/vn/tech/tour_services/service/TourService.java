package com.vn.tech.tour_services.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.vn.tech.tour_services.dto.TourDetailRequest;
import com.vn.tech.tour_services.dto.TourDetailResponse;
import com.vn.tech.tour_services.dto.TourResponse;

public interface TourService {

    List<TourResponse> searchTours(
        String q,
        String departures,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    );

    TourDetailResponse getTourDetail(String slug, TourDetailRequest request);
}
