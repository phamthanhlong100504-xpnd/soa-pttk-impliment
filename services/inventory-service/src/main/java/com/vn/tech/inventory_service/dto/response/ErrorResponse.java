package com.vn.tech.inventory_service.dto.response;

import com.sun.java.accessibility.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * The class ApiResponse implements received result from server
 * @author LongPT
 */
public class ErrorResponse extends ApiResponse{
    public ErrorResponse() {
        super(HttpStatus.BAD_REQUEST.value(), "Error");
    }

    public ErrorResponse(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public ErrorResponse(String message, Object data) {
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
