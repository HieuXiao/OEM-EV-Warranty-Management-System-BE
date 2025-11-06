package com.mega.warrantymanagementsystem.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ResendMailService {

    private final String API_KEY = "re_FRMu2e5d_Jnrxn7i2x4qbzms7tp9wdymF";
    private final String URL = "https://api.resend.com/emails";

    public void sendResetPasswordMail(String toEmail, String fullName, String resetUrl) {
        RestTemplate restTemplate = new RestTemplate();

        // tạo body JSON
        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // email đã xác thực trên Resend
        body.put("to", List.of(toEmail));
        body.put("subject", "Reset your password");
        body.put("html", "<p>Hi " + fullName + ",</p>"
                + "<p>You requested to reset your password. Click the link below:</p>"
                + "<p><a href='" + resetUrl + "'>Reset password</a></p>");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
            System.out.println("Email sent. Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send reset password email: " + e.getMessage());
        }
    }
}
