package com.vn.tech.booking_tour_service.entity;

import com.vn.tech.booking_tour_service.common.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.springframework.data.annotation.Id;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_outbox")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sendAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
