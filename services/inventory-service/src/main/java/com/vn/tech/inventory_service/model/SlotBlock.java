package com.vn.tech.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "slot_blocks", indexes = {
    // Ánh xạ chính xác Index từ file Liquibase để tối ưu truy vấn giải phóng vé
    @Index(name = "idx_blocks_status_expires", columnList = "status, expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // Liên kết với TourSchedule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_schedule_id", nullable = false)
    private TourSchedule tourSchedule;

    @Column(name = "customer_id")
    private UUID customerId; // <--- Đã sửa: Xóa @JdbcTypeCode

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SlotBlockStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "booking_id")
    private UUID bookingId; // <--- Đã sửa: Xóa @JdbcTypeCode

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SlotBlockStatus {
        PENDING,   // Đang giữ chỗ, chờ thanh toán
        CONFIRMED, // Đã thanh toán thành công, chốt vé
        EXPIRED    // Hết thời gian giữ chỗ (vd: quá 15 phút), hệ thống tự động nhả vé
    }
}
