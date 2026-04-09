package com.vn.tech.tour_services.service.impl;

import com.vn.tech.tour_services.dto.TourResponse;
import com.vn.tech.tour_services.entity.Tour;
import com.vn.tech.tour_services.repository.TourRepository;
import com.vn.tech.tour_services.repository.TourScheduleRepository;
import com.vn.tech.tour_services.service.TourService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourScheduleRepository tourScheduleRepository;

    @Override
    public List<TourResponse> searchTours(
        UUID destinationId,
        UUID departureId,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    ) {
        List<Tour> tours = tourRepository.findAll();
        final Set<UUID> tourIdsByStartDate = startDate == null
            ? Collections.emptySet()
            : new HashSet<>(tourScheduleRepository.findTourIdsByStartDate(startDate));

        return tours.stream()
            .filter(tour -> matchesUuid(destinationId, tour.getDestinationId()))
            .filter(tour -> matchesUuid(departureId, tour.getDepartureId()))
            .filter(tour -> matchesDuration(durationDays, tour.getDurationDays()))
            .filter(tour -> startDate == null || tourIdsByStartDate.contains(tour.getId()))
            .filter(tour -> matchesMinPrice(minPrice, tour.getBasePrice()))
            .filter(tour -> matchesMaxPrice(maxPrice, tour.getBasePrice()))
            .map(this::mapToResponse)
            .toList();
    }

    private boolean matchesDuration(Integer durationDays, Integer value) {
        return durationDays == null || Objects.equals(durationDays, value);
    }

    private boolean matchesUuid(UUID filter, UUID value) {
        return filter == null || Objects.equals(filter, value);
    }

    private boolean matchesMinPrice(BigDecimal minPrice, BigDecimal value) {
        return minPrice == null || (value != null && value.compareTo(minPrice) >= 0);
    }

    private boolean matchesMaxPrice(BigDecimal maxPrice, BigDecimal value) {
        return maxPrice == null || (value != null && value.compareTo(maxPrice) <= 0);
    }

    private TourResponse mapToResponse(Tour tour) {
        return TourResponse.builder()
            .id(tour.getId())
            .slug(tour.getSlug())
            .name(tour.getName())
            .description(tour.getDescription())
            .destinationId(tour.getDestinationId())
            .departureId(tour.getDepartureId())
            .basePrice(tour.getBasePrice())
            .durationDays(tour.getDurationDays())
            .durationNights(tour.getDurationNights())
            .status(tour.getStatus())
            .averageRating(tour.getAverageRating())
            .reviewCount(tour.getReviewCount())
            .createdAt(tour.getCreatedAt())
            .updatedAt(tour.getUpdatedAt())
            .build();
    }
}
