package com.vn.tech.booking_tour_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tbl_tour_schedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleEntity {
    private UUID id;
    private String code;
}
