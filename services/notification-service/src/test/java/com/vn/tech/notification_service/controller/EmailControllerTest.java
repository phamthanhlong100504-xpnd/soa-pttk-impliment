package com.vn.tech.notification_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.tech.notification_service.dto.EmailRequest;
import com.vn.tech.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSendEmailSuccess() throws Exception {
        EmailRequest emailRequest = new EmailRequest(
            "user@example.com",
            "Nguyễn Văn A",
            "Xác nhận đơn hàng",
            "Cảm ơn bạn đã đặt hàng"
        );

        mockMvc.perform(post("/api/v1/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Nguyễn Văn A"));
    }

    @Test
    public void testSendEmailMissingEmail() throws Exception {
        EmailRequest emailRequest = new EmailRequest(
            null,
            "Nguyễn Văn A",
            "Xác nhận đơn hàng",
            "Cảm ơn bạn đã đặt hàng"
        );

        mockMvc.perform(post("/api/v1/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Email không được để trống"));
    }

    @Test
    public void testSendEmailMissingName() throws Exception {
        EmailRequest emailRequest = new EmailRequest(
            "user@example.com",
            null,
            "Xác nhận đơn hàng",
            "Cảm ơn bạn đã đặt hàng"
        );

        mockMvc.perform(post("/api/v1/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Tên người nhập không được để trống"));
    }
}
