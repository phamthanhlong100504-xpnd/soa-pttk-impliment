package com.vn.tech.inventory_service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The class ApiResponse implements received result from server
 * @author LongPT
 */
public class ApiResponse extends ResponseEntity<ApiResponse.Payload> {

    public ApiResponse(int code, String message) {
        super(new Payload(code, message, null), HttpStatus.OK);
    }

    public ApiResponse(int code, String message, Object data) {
        super(new Payload(code, message, data), HttpStatus.OK);
    }

    @Value
    @AllArgsConstructor
    @Builder
    public static class Payload {
        private int code;
        private String message;
        private Object data;
    }
}
