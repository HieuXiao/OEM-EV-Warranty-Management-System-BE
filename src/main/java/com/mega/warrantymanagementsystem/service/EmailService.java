package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.model.EmailDetail;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final String API_KEY = "re_FRMu2e5d_Jnrxn7i2x4qbzms7tp9wdymF"; //Resend API Key của bạn
    private final String URL = "https://api.resend.com/emails";

    public void sendMailTemplate(EmailDetail emailDetail, String templateName) {
        try {
            // đọc nội dung HTML template từ resources/templates
            ClassPathResource resource = new ClassPathResource("templates/" + templateName);
            String html = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

            // thay thế biến trong template (tương tự Thymeleaf cơ bản)
            html = html.replace("[[${name}]]", emailDetail.getFullName() != null ? emailDetail.getFullName() : "")
                    .replace("[[${url}]]", emailDetail.getUrl() != null ? emailDetail.getUrl() : "");

            // body JSON gửi đến Resend
            Map<String, Object> body = new HashMap<>();
            body.put("from", "onboarding@resend.dev"); // phải là email đã xác thực trên Resend
            body.put("to", List.of(emailDetail.getRecipient()));
            body.put("subject", emailDetail.getSubject());
            body.put("html", html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
            System.out.println("Email sent successfully: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

}
