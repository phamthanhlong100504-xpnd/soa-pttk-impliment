package com.vn.tech.tour_services.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vn.tech.tour_services.dto.TourDetailResponse;
import com.vn.tech.tour_services.dto.TourItineraryResponse;
import com.vn.tech.tour_services.dto.TourResponse;
import com.vn.tech.tour_services.dto.TourScheduleResponse;
import com.vn.tech.tour_services.dto.TourSearchResponse;
import com.vn.tech.tour_services.entity.Tour;
import com.vn.tech.tour_services.entity.TourItinerary;
import com.vn.tech.tour_services.repository.TourItineraryRepository;
import com.vn.tech.tour_services.repository.TourRepository;
import com.vn.tech.tour_services.repository.TourScheduleRepository;
import com.vn.tech.tour_services.service.TourService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourScheduleRepository tourScheduleRepository;
    private final TourItineraryRepository tourItineraryRepository;

    @Override
    public List<TourSearchResponse> searchTours(
        String q,
        String departures,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    ) {
        log.info(
            "[tour-service] searchTours called q={}, departures={}, startDate={}, durationDays={}, minPrice={}, maxPrice={}",
            q, departures, startDate, durationDays, minPrice, maxPrice
        );

        String normalizedQuery = slugify(q);
        String normalizedDeparture = normalize(departures);
        log.info("[tour-service] searchTours normalizedQuery={}, normalizedDeparture={}", normalizedQuery, normalizedDeparture);

        List<Tour> tours = tourRepository.searchTours(
            normalizedQuery,
            normalizedDeparture,
            startDate,
            durationDays,
            minPrice,
            maxPrice
        );

        log.info("[tour-service] searchTours matchedTours={}", tours.size());
        if (!tours.isEmpty()) {
            log.info("[tour-service] searchTours matchedTourIds={}", tours.stream().map(Tour::getId).toList());
            Map<UUID, Tour> uniqueTours = new LinkedHashMap<>();
            for (Tour tour : tours) {
                uniqueTours.putIfAbsent(tour.getId(), tour);
            }
            if (uniqueTours.size() != tours.size()) {
                log.warn("[tour-service] searchTours deduplicated tours from {} to {}", tours.size(), uniqueTours.size());
            }
            List<Tour> distinctTours = new ArrayList<>(uniqueTours.values());
            tours = distinctTours;
            log.info("[tour-service] searchTours distinctTourIds={}", tours.stream().map(Tour::getId).toList());
        }

        if (tours.isEmpty()) {
            log.info("[tour-service] searchTours returning empty result");
            return Collections.emptyList();
        }

        log.info("[tour-service] searchTours skip schedule loading for list endpoint");

        return tours.stream()
            .map(this::mapToSearchResponse)
            .toList();
    }

    @Override
    public TourDetailResponse getTourDetail(String slug, UUID tourId) {
        log.info("[tour-service] getTourDetail called with slug={}, headerTourId={}", slug, tourId);

        Tour tour;
        if (tourId != null) {
            tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));
            if (slug != null && !slug.equals(tour.getSlug())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slug does not match tour_id");
            }
        } else {
            tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found: " + slug));
        }

        UUID queryTourId = tour.getId();

        log.info("[tour-service] getTourDetail resolvedTourId={}, resolvedSlug={}, resolvedDepartureId={}, resolvedDestinationId={}",
            tour.getId(), tour.getSlug(), tour.getDepartureId(), tour.getDestinationId());

        List<TourScheduleResponse> schedules = tourScheduleRepository.findByTourIdOrderByStartDateAsc(queryTourId)
            .stream()
            .map(this::mapSchedule)
            .toList();

        List<TourItineraryResponse> itinerary = tourItineraryRepository.findByTourIdOrderByDayNumberAsc(queryTourId)
            .stream()
            .map(this::mapItinerary)
            .toList();

        log.info("[tour-service] Resolved {} schedules for slug={}", schedules.size(), slug);
        if (schedules.isEmpty()) {
            log.warn("[tour-service] No schedules found for slug={}", slug);
        } else {
            log.info("[tour-service] Schedule codes for slug={} => {}", slug, schedules.stream().map(TourScheduleResponse::getCode).toList());
        }

        TourResponse tourData = mapToResponse(tour, schedules);

        return TourDetailResponse.builder()
            .tour(tourData)
            .tourItinerary(itinerary)
            .build();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String slugify(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }

        return Pattern.compile("\\s+").matcher(normalized.toLowerCase()).replaceAll("-");
    }

    private TourResponse mapToResponse(Tour tour, List<TourScheduleResponse> schedules) {
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
            .tourSchedules(schedules == null ? Collections.emptyList() : schedules)
            .build();
    }

    private TourSearchResponse mapToSearchResponse(Tour tour) {
        return TourSearchResponse.builder()
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

    private TourScheduleResponse mapSchedule(com.vn.tech.tour_services.entity.TourSchedule schedule) {
        return TourScheduleResponse.builder()
            .id(schedule.getId())
            .tourId(schedule.getTourId())
            .code(schedule.getCode())
            .startDate(schedule.getStartDate())
            .endDate(schedule.getEndDate())
            .price(schedule.getPrice())
            .maxParticipants(schedule.getMaxParticipants())
            .minParticipants(schedule.getMinParticipants())
            .status(schedule.getStatus())
            .build();
    }

    private TourItineraryResponse mapItinerary(TourItinerary itinerary) {
        return TourItineraryResponse.builder()
            .id(itinerary.getId())
            .tourId(itinerary.getTourId())
            .dayNumber(itinerary.getDayNumber())
            .title(itinerary.getTitle())
            .description(itinerary.getDescription())
            .meals(itinerary.getMeals())
            .activities(itinerary.getActivities())
            .build();
    }

}
