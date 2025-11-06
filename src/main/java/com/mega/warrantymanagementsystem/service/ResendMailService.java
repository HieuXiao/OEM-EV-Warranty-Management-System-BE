package com.mega.warrantymanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // email test của Resend
        body.put("to", List.of(toEmail));
        body.put("subject", "Reset your password");

        // HTML template dài nhưng đã escape
        String htmlTemplate = """
        <html lang='en' style='margin:0; padding:0;'>
        <head>
            <meta charset='utf-8'>
            <title>Password Reset Request</title>
            <style>
                .btn { background:#007bff; color:#ffffff; padding:12px 18px; border-radius:6px; text-decoration:none; font-weight:700; display:inline-block; }
            </style>
        </head>
        <body style='margin:0; padding:0; background:#f6f6f6; font-family:Arial,sans-serif;'>
        <table width='100%' cellpadding='0' cellspacing='0'>
        <tr><td align='center'>
        <table width='600' style='background:#ffffff; border-radius:8px; padding:20px;'>
        <tr><td>
        <p>Hello <strong>%s</strong>,</p>
        <p>We received a request to reset your password. Click the button below:</p>
        <p><a href='%s' class='btn'>Reset Password</a></p>
        <p>If you did not request this, ignore this email.</p>
        </td></tr>
        </table>
        </td></tr>
        </table>
        </body>
        </html>
        """;

        // escape ký tự " và newlines
        String html = htmlTemplate.formatted(fullName, resetUrl)
                .replace("\"", "\\\"")
                .replace("\n", "");

        body.put("html", html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        try {
            // serialize JSON body để Resend nhận chính xác
            String jsonBody = mapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
            System.out.println("Email sent. Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send reset password email: " + e.getMessage());
        }
    }
}
