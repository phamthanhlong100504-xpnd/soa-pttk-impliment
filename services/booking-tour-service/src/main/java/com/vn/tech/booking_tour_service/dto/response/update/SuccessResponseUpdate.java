package com.vn.tech.booking_tour_service.dto.response.update;

public class SuccessResponseUpdate extends ApiResponseUpdate {

    /**
     * Create a new {@code SuccessResponse}.
     */
    public SuccessResponseUpdate() {
        super(200,"success");
    }

    /**
     * Create a new {@code SuccessResponse} with the given data.
     * @param data
     */
    public SuccessResponseUpdate(Object data) {
        super(200, ("success"), data);
    }

    public SuccessResponseUpdate(Object data, String message) {
        super(200, message, data);
    }

}
