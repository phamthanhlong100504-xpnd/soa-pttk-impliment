package com.vn.tech.booking_tour_service.common;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    DEAD_LETTER
}
