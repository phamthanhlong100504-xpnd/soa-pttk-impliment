package com.vn.tech.inventory_service.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR) // <--- Thêm dòng này. Cơ sở: Ép Hibernate bind tham số JDBC dưới dạng chuỗi VARCHAR.
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    // Quan hệ 1-N với TourSchedule. CascadeType.ALL giúp lưu/xóa đồng bộ.
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TourSchedule> schedules = new ArrayList<>();
}
