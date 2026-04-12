package com.vn.tech.booking_tour_service.dto.response.update;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ErrorResponseUpdate extends ApiResponseUpdate {
    public ErrorResponseUpdate() {
        super(HttpStatus.BAD_REQUEST.value(), "Error");
    }

    public ErrorResponseUpdate(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public ErrorResponseUpdate(String message, Object data) {
        super(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    @Value
    @AllArgsConstructor
    public static class ApiError {
        List<Error> errors;
    }

    @Value
    @AllArgsConstructor
    public static class Error {
        private String field;
        private Object value;
        private String message;
    }
}
