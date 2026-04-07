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

        context.setVariable("bookingId", param.getBookingId()); // Bạn đang thiếu dòng này
        context.setVariable("customerName", param.getCustomerName());
        context.setVariable("email", param.getEmail());
        context.setVariable("name_tour", param.getName_tour());
        context.setVariable("start_date", param.getStart_date());
        context.setVariable("end_date", param.getEnd_date());
        context.setVariable("cost", param.getCost());
        log.info("[BookingTicketRenderService] - Chi tiết thông tin : customerName : {}; email : {}; cost : {}; tourname : {}; startDate : {}; endDate : {}",
            param.getCustomerName(),param.getEmail(), param.getCost(), param.getName_tour(),param.getStart_date(), param.getEnd_date());

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
