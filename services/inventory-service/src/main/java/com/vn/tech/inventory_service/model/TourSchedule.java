package com.vn.tech.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tour_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // Quan hệ 1-1 với Inventory.
    @OneToOne(mappedBy = "tourSchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Inventory inventory;

    @OneToMany(mappedBy = "tourSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SlotBlock> slotBlocks = new ArrayList<>();
}
