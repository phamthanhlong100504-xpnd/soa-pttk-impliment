package com.vn.tech.booking_tour_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InventoryApiResponse extends ResponseEntity<InventoryApiResponse.Payload> {

    public InventoryApiResponse(int code, String message) {
        super(new Payload(code, message, null), HttpStatus.OK);
    }

    public InventoryApiResponse(int code, String message, Object data) {
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
