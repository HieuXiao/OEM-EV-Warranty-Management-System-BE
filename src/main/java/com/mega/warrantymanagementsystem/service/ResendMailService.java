package com.mega.warrantymanagementsystem.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ResendMailService {

    // hard‑code API key Resend
    private final String API_KEY = "re_FRMu2e5d_Jnrxn7i2x4qbzms7tp9wdymF";
    private final String URL = "https://api.resend.com/emails";

    public void sendResetPasswordMail(String toEmail, String fullName, String resetUrl) {
        RestTemplate restTemplate = new RestTemplate();

        // tạo body JSON
        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // email đã xác thực trên Resend
        body.put("to", List.of(toEmail));
        body.put("subject", "Reset your password");

        // template HTML reset password
        String htmlTemplate = """
        <!doctype html>
        <html lang="en" style="margin:0; padding:0;">
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Password Reset Request – Project Name</title>
            <style>
                .hover-underline:hover { text-decoration: underline !important; }
                .btn { background:#007bff; color:#ffffff; padding:12px 18px; border-radius:6px; text-decoration:none; display:inline-block; font-weight:700; }
                a[x-apple-data-detectors] { color: inherit !important; text-decoration: none !important; }
            </style>
        </head>
        <body style="margin:0; padding:0; width:100% !important; background:#f6f6f6;">
        <table role="presentation" width="100%" cellspacing="0" cellpadding="0">
        <tr><td align="center">
        <table role="presentation" width="600" style="border-radius:8px; background:#ffffff; padding:24px;">
        <tr>
            <td>
                <p>Hello <strong>%s</strong>,</p>
                <p>We received a request to reset the password for your account associated with this email address.</p>
                <p>To proceed, please click the button below. This link will expire in <strong>24 hours</strong> for your security.</p>
                <p><a href="%s" class="btn">Reset Password</a></p>
                <p>If you did not request this, you can safely ignore this email.</p>
            </td>
        </tr>
        </table>
        </td></tr>
        </table>
        </body>
        </html>
        """;

        // chèn tên và link vào template
        String html = htmlTemplate.formatted(fullName, resetUrl);
        body.put("html", html);

        // header cho request
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
