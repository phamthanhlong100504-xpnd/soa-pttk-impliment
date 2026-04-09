package com.vn.tech.tour_services.service.impl;

import com.vn.tech.tour_services.repository.TourRepository;
import com.vn.tech.tour_services.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
}
