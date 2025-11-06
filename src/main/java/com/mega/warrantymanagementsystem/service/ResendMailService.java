package com.mega.warrantymanagementsystem.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ResendMailService {

    private static final String API_KEY = "re_FRMu2e5d_Jnrxn7i2x4qbzms7tp9wdymF";
    private static final String URL = "https://api.resend.com/emails";

    public void sendResetPasswordMail(String toEmail, String fullName, String resetUrl) {
        RestTemplate restTemplate = new RestTemplate();

        //fix bung deploy
        //6-11
        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // địa chỉ đã verify trong Resend
        body.put("to", List.of(toEmail));
        body.put("subject", "Reset your password");
        body.put("html",
                "<p>Xin chào " + fullName + ",</p>"
                        + "<p>Bạn vừa yêu cầu đặt lại mật khẩu. Nhấn vào liên kết dưới đây để đặt lại:</p>"
                        + "<p><a href='" + resetUrl + "'>Đặt lại mật khẩu</a></p>"
                        + "<p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
            System.out.println("Email sent successfully. Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
