package com.university.document_service.controller;

import com.university.document_service.model.BookingTicketRequest;
import com.university.document_service.service.BookingTicketRenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents/booking-tickets")
public class BookingTicketController {


    private final BookingTicketRenderService bookingTicketRenderService;

    public BookingTicketController(BookingTicketRenderService bookingTicketRenderService) {
        this.bookingTicketRenderService = bookingTicketRenderService;
    }

    @PostMapping("/show-ticket")
    public ResponseEntity<byte[]> renderViaPost(@RequestBody BookingTicketRequest request) {
        log.info("[Document Service] : Nhận được yêu cầu hiển thị thông tin đặt tour của tour : {}; user : {}",request.getBookingId(),request.getTourName());
        return processRender(request);
    }

    @PostMapping("/file-pdf-ticket")
    public ResponseEntity<byte[]> getPdfPost(@RequestBody BookingTicketRequest request) {
        log.info("[Document Service] : Nhận được yêu cầu tạo file pdf đặt tour của tour : {}; user : {}",request.getBookingId(),request.getTourName());
        return getFilePdf(request);
    }

    private ResponseEntity<byte[]> getFilePdf(BookingTicketRequest param) {
        try {
            byte[] pdfBytes = bookingTicketRenderService.generatePdf(param);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);

            // Sử dụng "attachment" để bắt buộc tải về máy
            // filename sẽ đặt tên cho file khi tải về
            headers.setContentDispositionFormData("attachment", "Ve_Tour_" + param.getBookingId() + ".pdf");

            // Cache-Control để đảm bảo trình duyệt không dùng file cũ
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
        } catch (Exception e) {
            log.error("Lỗi render PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<byte[]> processRender(BookingTicketRequest request) {
        try {
            byte[] pdfBytes = bookingTicketRenderService.generatePdf(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);

            // Sử dụng "attachment" để tải về máy
            headers.setContentDispositionFormData("inline", "Bill_" + request.getCustomerName() + "_code" + request.getBookingId() + ".pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
        } catch (Exception e) {
            log.error("Lỗi render PDF: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
