package com.university.document_service.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.university.document_service.model.BookingTicketRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;

@Slf4j
@Service
public class BookingTicketRenderService {
    private final TemplateEngine templateEngine;

    public BookingTicketRenderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(BookingTicketRequest param) throws Exception {
        // 1. Nạp tham số vào Template Context

        Context context = new Context();

        // 1. Nạp các biến đơn lẻ
        context.setVariable("bookingId", param.getBookingId());
        context.setVariable("accountId", param.getAccountId());
        context.setVariable("tourScheduleId", param.getTourScheduleId());
        context.setVariable("tourName", param.getTourName());
        context.setVariable("quantity", param.getQuantity());
        context.setVariable("confirmedSlots", param.getConfirmedSlots());
        context.setVariable("totalPrice", param.getTotalPrice());

        // 2. Nạp CẢ DANH SÁCH (Rất quan trọng)
        context.setVariable("passengers", param.getPassengers());
        context.setVariable("optionalServices", param.getOptionalServices());

        log.info("[BookingTicketRenderService] - Generate PDF cho Booking: {}, Tour: {}, Số lượng: {}",
            param.getBookingId(), param.getTourName(), param.getQuantity());

        // 2. Build HTML String
        String html = templateEngine.process("booking_ticket", context);

        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf");

        if (fontStream == null) {
            throw new IOException("Không tìm thấy file font trong resources/fonts/arial.ttf");
        }

        byte[] fontBytes = fontStream.readAllBytes();

        // 3. Render ra mảng Byte PDF
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(() -> new ByteArrayInputStream(fontBytes), "Arial");
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            return os.toByteArray();
        }
    }
}
