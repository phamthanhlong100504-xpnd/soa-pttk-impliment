package com.vn.tech.booking_tour_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_EXIST(1002, "Booking not exist", HttpStatus.BAD_REQUEST),
    GET_TOUR_SCHEDULE_AVAILABLE_SLOT_FAIL(1003, "Get tour schedule available slot fail", HttpStatus.BAD_REQUEST),
    TOUR_SCHEDULE_NOT_ENOUGH_SLOTS(1004, "Tour schedule not enough slots", HttpStatus.BAD_REQUEST),
    CREATE_INVENTORY_BLOCK_SLOTS_FAIL(1005, "Create inventory block slots fail", HttpStatus.BAD_REQUEST),
    CREATE_BOOKING_FAIL(1006, "Create booking fail", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
