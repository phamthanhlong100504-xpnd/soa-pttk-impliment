package com.vn.tech.booking_tour_service.exception;

import com.vn.tech.booking_tour_service.dto.ApiResponseCreate;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponseCreate> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);
        ApiResponseCreate apiResponseCreate = new ApiResponseCreate();

        apiResponseCreate.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponseCreate.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponseCreate);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponseCreate> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponseCreate apiResponseCreate = new ApiResponseCreate();

        apiResponseCreate.setCode(errorCode.getCode());
        apiResponseCreate.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponseCreate);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponseCreate> handlingValidation(MethodArgumentNotValidException exception) {

        var fieldError = exception.getFieldError();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        if (fieldError != null) {
            try {
                errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());

                var error = exception.getBindingResult().getAllErrors().get(0);
                if (error.contains(ConstraintViolation.class)) {
                    attributes = error.unwrap(ConstraintViolation.class)
                        .getConstraintDescriptor()
                        .getAttributes();
                }
            } catch (IllegalArgumentException ignored) {}
        }

        ApiResponseCreate apiResponseCreate = new ApiResponseCreate();
        apiResponseCreate.setCode(errorCode.getCode());
        apiResponseCreate.setMessage(
            attributes != null
                ? mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage()
        );

        return ResponseEntity.badRequest().body(apiResponseCreate);
    }


    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}

