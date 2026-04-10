package com.vn.tech.tour_services.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vn.tech.tour_services.dto.ApiResponse;
import com.vn.tech.tour_services.dto.TourDetailRequest;
import com.vn.tech.tour_services.dto.TourDetailResponse;
import com.vn.tech.tour_services.dto.TourResponse;
import com.vn.tech.tour_services.service.TourService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tours")
@Slf4j
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ApiResponse<List<TourResponse>> searchTours(
        @RequestParam(name = "q", required = false) String q,
        @RequestParam(name = "departures", required = false) String departures,
        @RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "duration_days", required = false) Integer durationDays,
        @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
        @RequestParam(name = "max_price", required = false) BigDecimal maxPrice
    ) {
        log.info(
            "searchTours request q={}, departures={}, startDate={}, durationDays={}, minPrice={}, maxPrice={}",
            q, departures, startDate, durationDays, minPrice, maxPrice
        );

        List<TourResponse> tours = tourService.searchTours(
            q,
            departures,
            startDate,
            durationDays,
            minPrice,
            maxPrice
        );

        log.info("searchTours responseCount={}, firstTourId={}",
            tours.size(),
            tours.isEmpty() ? null : tours.get(0).getId());

        return ApiResponse.<List<TourResponse>>builder()
            .success(true)
            .data(tours)
            .message("OK")
            .build();
    }

    @GetMapping("/{slug}")
    public ApiResponse<TourDetailResponse> getTourDetail(
        @PathVariable("slug") String slug,
        @RequestHeader(name = "X-Tour-Id", required = false) String tourIdHeader
    ) {
        log.info("getTourDetail request slug={}, rawHeaderTourId={}", slug, tourIdHeader);

        UUID tourId = parseTourIdHeader(tourIdHeader);
        log.info("getTourDetail parsedHeaderTourId={}", tourId);

        TourDetailRequest request = null;
        if (tourId != null) {
            request = new TourDetailRequest();
            request.setTourId(tourId);
        }

        TourDetailResponse data = tourService.getTourDetail(slug, request);

        log.info("getTourDetail response slug={}, tourId={}", slug, data.getTour() == null ? null : data.getTour().getId());

        return ApiResponse.<TourDetailResponse>builder()
            .success(true)
            .data(data)
            .message("OK")
            .build();
    }

    private UUID parseTourIdHeader(String tourIdHeader) {
        if (tourIdHeader == null || tourIdHeader.isBlank()) {
            return null;
        }

        try {
            return UUID.fromString(tourIdHeader.trim());
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid X-Tour-Id header format: {}", tourIdHeader);
            return null;
        }
    }
}
