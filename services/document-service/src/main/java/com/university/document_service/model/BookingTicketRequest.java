package com.university.document_service.model;

import lombok.Data;

@Data
public class BookingTicketRequest {
    private String bookingId;
    private String customerName;
    private String email;
    private String name_tour;
    private String start_date;
    private String end_date;
    private String cost;
    // add new here
}
