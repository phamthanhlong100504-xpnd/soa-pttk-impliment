package com.vn.tech.tour_services.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vn.tech.tour_services.dto.TourDetailRequest;
import com.vn.tech.tour_services.dto.TourDetailResponse;
import com.vn.tech.tour_services.dto.TourResponse;
import com.vn.tech.tour_services.dto.TourScheduleResponse;
import com.vn.tech.tour_services.entity.Tour;
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

    @Override
    public List<TourResponse> searchTours(
        String q,
        String departures,
        LocalDate startDate,
        Integer durationDays,
        BigDecimal minPrice,
        BigDecimal maxPrice
    ) {
        log.info(
            "searchTours called q={}, departures={}, startDate={}, durationDays={}, minPrice={}, maxPrice={}",
            q, departures, startDate, durationDays, minPrice, maxPrice
        );

        String normalizedQuery = slugify(q);
        String normalizedDeparture = normalize(departures);
        log.info("searchTours normalizedQuery={}, normalizedDeparture={}", normalizedQuery, normalizedDeparture);

        List<Tour> tours = tourRepository.searchTours(
            normalizedQuery,
            normalizedDeparture,
            startDate,
            durationDays,
            minPrice,
            maxPrice
        );

        log.info("searchTours matchedTours={}", tours.size());
        if (!tours.isEmpty()) {
            log.info("searchTours matchedTourIds={}", tours.stream().map(Tour::getId).toList());
            Map<UUID, Tour> uniqueTours = new LinkedHashMap<>();
            for (Tour tour : tours) {
                uniqueTours.putIfAbsent(tour.getId(), tour);
            }
            if (uniqueTours.size() != tours.size()) {
                log.warn("searchTours deduplicated tours from {} to {}", tours.size(), uniqueTours.size());
            }
            List<Tour> distinctTours = new ArrayList<>(uniqueTours.values());
            tours = distinctTours;
            log.info("searchTours distinctTourIds={}", tours.stream().map(Tour::getId).toList());
        }

        if (tours.isEmpty()) {
            log.info("searchTours returning empty result");
            return Collections.emptyList();
        }

        Map<UUID, List<TourScheduleResponse>> schedulesByTourId = buildSchedulesByTourId(
            tours.stream().map(Tour::getId).toList()
        );

        return tours.stream()
            .map(tour -> mapToResponse(tour, schedulesByTourId.getOrDefault(tour.getId(), Collections.emptyList())))
            .toList();
    }

    @Override
    public TourDetailResponse getTourDetail(String slug, TourDetailRequest request) {
        log.info("getTourDetail called with slug={}, headerTourId={}", slug, request == null ? null : request.getTourId());

        Tour tour = tourRepository.findBySlug(slug)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));

        log.info("getTourDetail resolvedTourId={}, resolvedDepartureId={}, resolvedDestinationId={}",
            tour.getId(), tour.getDepartureId(), tour.getDestinationId());

        if (request != null && request.getTourId() != null && !Objects.equals(request.getTourId(), tour.getId())) {
            log.warn("Ignoring tour_id mismatch for slug={}: headerTourId={}, resolvedTourId={}",
                slug, request.getTourId(), tour.getId());
        }

        List<TourScheduleResponse> schedules = tourScheduleRepository.findByTourSlugOrderByStartDateAsc(slug)
            .stream()
            .map(this::mapSchedule)
            .toList();

        log.info("Resolved {} schedules for slug={}", schedules.size(), slug);
        if (schedules.isEmpty()) {
            log.warn("No schedules found for slug={}", slug);
        } else {
            log.info("Schedule codes for slug={} => {}", slug, schedules.stream().map(TourScheduleResponse::getCode).toList());
        }

        TourResponse tourData = mapToResponse(tour, schedules);

        return TourDetailResponse.builder()
            .tour(tourData)
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

    private Map<UUID, List<TourScheduleResponse>> buildSchedulesByTourId(List<UUID> tourIds) {
        if (tourIds.isEmpty()) {
            log.info("buildSchedulesByTourId called with empty tourIds");
            return Collections.emptyMap();
        }

        log.info("buildSchedulesByTourId fetching schedules for tourIds={}", tourIds);

        List<TourScheduleResponse> scheduleRows = tourScheduleRepository.findByTourIdIn(tourIds)
            .stream()
            .map(this::mapSchedule)
            .sorted(Comparator.comparing(TourScheduleResponse::getStartDate, Comparator.nullsLast(LocalDate::compareTo)))
            .toList();

        log.info("buildSchedulesByTourId fetchedScheduleRows={}", scheduleRows.size());

        Map<UUID, List<TourScheduleResponse>> grouped = new LinkedHashMap<>();
        for (TourScheduleResponse schedule : scheduleRows) {
            grouped.computeIfAbsent(schedule.getTourId(), ignored -> new ArrayList<>())
                .add(schedule);
        }
        return grouped;
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

}
