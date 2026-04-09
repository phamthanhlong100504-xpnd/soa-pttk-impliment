package com.vn.tech.tour_services.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.vn.tech.tour_services.dto.TourResponse;

public interface TourService {

    List<TourResponse> searchTours(
        UUID destinationId,
        UUID departureId,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    );
}
