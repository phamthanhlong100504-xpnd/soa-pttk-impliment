package com.vn.tech.tour_services.controller;

import com.vn.tech.tour_services.dto.ApiResponse;
import com.vn.tech.tour_services.dto.TourResponse;
import com.vn.tech.tour_services.service.TourService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ApiResponse<List<TourResponse>> searchTours(
        @RequestParam(name = "destination_id", required = false) UUID destinationId,
        @RequestParam(name = "departure_id", required = false) UUID departureId,
        @RequestParam(name = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "duration_days", required = false) Integer durationDays,
        @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
        @RequestParam(name = "max_price", required = false) BigDecimal maxPrice
    ) {
        List<TourResponse> tours = tourService.searchTours(
            destinationId,
            departureId,
            startDate,
            durationDays,
            minPrice,
            maxPrice
        );

        return ApiResponse.<List<TourResponse>>builder()
            .success(true)
            .data(tours)
            .message("OK")
            .build();
    }
}
