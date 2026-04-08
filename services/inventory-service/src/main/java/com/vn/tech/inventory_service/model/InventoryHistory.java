package com.vn.tech.inventory_service.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "inventory_history")
@EntityListeners(AuditingEntityListener.class) // Tự động điền ngày tạo
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR) // <--- Thêm dòng này. Cơ sở: Ép Hibernate bind tham số JDBC dưới dạng chuỗi VARCHAR.
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private UUID id;

    // Liên kết với Inventory. Dùng FetchType.LAZY để tối ưu hiệu suất
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_schedule_id", nullable = false)
    private Inventory inventory;

    // Ghi nhận loại hành động: Đặt vé, Hủy vé, Khóa slot...
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 50)
    private ActionType actionType;

    // Số lượng thay đổi: Có thể là số âm (trừ slot khi book) hoặc dương (cộng slot khi hủy)
    @Column(name = "quantity_changed", nullable = false)
    private Integer quantityChanged;

    // Lưu lại trạng thái slot TRƯỚC khi thay đổi (Dùng để đối soát)
    @Column(name = "previous_available_slots", nullable = false)
    private Integer previousAvailableSlots;

    // Lưu lại trạng thái slot SAU khi thay đổi
    @Column(name = "new_available_slots", nullable = false)
    private Integer newAvailableSlots;

    // Mã tham chiếu đến Booking ID, Order ID hoặc Ticket ID gây ra sự thay đổi này
    @Column(name = "reference_id")
    private String referenceId;

    // User ID hoặc System Service nào thực hiện hành động này
    @Column(name = "actor", nullable = false)
    private String actor;

    @Column(name = "note")
    private String note;

    // Thời gian xảy ra biến động
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Enum định nghĩa các loại giao dịch
    public enum ActionType {
        BLOCK,
        CONFIRM,
        EXPIRE,
        ADJUST,
        CANCEL
    }
}
