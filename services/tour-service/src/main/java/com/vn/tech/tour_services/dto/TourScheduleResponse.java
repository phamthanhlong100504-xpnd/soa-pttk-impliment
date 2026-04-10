package com.vn.tech.tour_services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourScheduleResponse {

    private UUID id;

    @JsonProperty("tour_id")
    private UUID tourId;

    private String code;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private BigDecimal price;

    @JsonProperty("max_participants")
    private Integer maxParticipants;

    @JsonProperty("min_participants")
    private Integer minParticipants;

    private String status;
}
