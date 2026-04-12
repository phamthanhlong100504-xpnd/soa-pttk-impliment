package com.vn.tech.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @Column(name = "tour_schedule_id", updatable = false, nullable = false)
    private UUID id; // Primary key chia sẻ từ TourSchedule

    // Cơ chế @MapsId gắn PK của bảng này với FK tour_schedule_id
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tour_schedule_id")
    private TourSchedule tourSchedule;

    // Denormalization: Lưu sẵn tour_id để query nhanh không cần JOIN qua bảng trung gian
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;

    @Column(name = "blocked_slots", nullable = false)
    private Integer blockedSlots;

    @Column(name = "confirmed_slots", nullable = false)
    private Integer confirmedSlots;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold;

    // Cơ chế Optimistic Locking để chống overbooking (bán lố vé)
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryHistory> histories = new ArrayList<>();
}
