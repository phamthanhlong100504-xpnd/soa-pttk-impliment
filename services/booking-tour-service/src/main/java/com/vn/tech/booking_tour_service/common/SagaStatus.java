package com.vn.tech.booking_tour_service.common;

public enum SagaStatus {
    // Initial states
    INITIATED,

    // Validation phase
    VALIDATING_TOUR,
    TOUR_VALIDATED,
    TOUR_VALIDATION_FAILED,

    // Inventory blocking phase
    BLOCKING_INVENTORY,
    INVENTORY_BLOCKED,
    INVENTORY_BLOCK_FAILED,

    // Booking phase
    CREATING_BOOKING,
    BOOKING_CREATED,
    BOOKING_FAILED,

    // Awaiting payment
    AWAITING_PAYMENT,
    PAYMENT_TIMEOUT,

    // Payment received
    PAYMENT_RECEIVED,
    PAYMENT_FAILED,

    // Confirmation phase
    CONFIRMING_BOOKING,
    BOOKING_CONFIRMED,
    BOOKING_CONFIRM_FAILED,

    // Inventory confirmation phase
    CONFIRMING_INVENTORY,
    INVENTORY_CONFIRMED,
    INVENTORY_CONFIRM_FAILED,

    // Final states
    COMPLETED,
    FAILED,
    COMPENSATING,
    COMPENSATED,
    CANCELLED
}
