package com.mega.warrantymanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        ObjectMapper mapper = new ObjectMapper();

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

            // HTML template giống template bạn gửi
            String html = ""
                    + "<!doctype html>"
                    + "<html lang='en' style='margin:0; padding:0;'>"
                    + "<head>"
                    + "<meta charset='utf-8'>"
                    + "<title>Password Reset Request – Project Name</title>"
                    + "<style>"
                    + ".btn { background:#007bff;color:#fff;padding:12px 18px;border-radius:6px;text-decoration:none;font-weight:700;display:inline-block;}"
                    + "</style>"
                    + "</head>"
                    + "<body style='margin:0;padding:0;background:#f6f6f6;font-family:Arial,sans-serif;'>"
                    + "<div style='display:none;overflow:hidden;line-height:1px;opacity:0;max-height:0;max-width:0;'>You requested to reset your password – click the link below to continue.</div>"
                    + "<table width='100%' cellpadding='0' cellspacing='0'><tr><td align='center'>"
                    + "<table width='600' style='background:#ffffff;border-radius:8px;padding:20px;'>"
                    + "<tr><td align='center' style='background:#38414a;color:#ffffff;padding:24px 16px;border-radius:8px 8px 0 0;'>"
                    + "<a href='https://example.com' style='font-size:26px;font-weight:700;color:#ffffff;text-decoration:none;'>Project Name</a>"
                    + "<div style='font-size:14px;margin-top:8px;color:#ffffff;opacity:.9;'>Password Reset Request</div>"
                    + "</td></tr>"
                    + "<tr><td style='padding:24px 20px;'>"
                    + "<p>Hello <strong>" + fullName + "</strong>,</p>"
                    + "<p>We received a request to reset the password for your account associated with this email address.</p>"
                    + "<p>To proceed, please click the button below. This link will expire in <strong>24 hours</strong> for your security.</p>"
                    + "<p><a href='" + resetUrl + "' class='btn'>Reset Password</a></p>"
                    + "<p style='font-size:13px;color:#777777;'>If you did not request a password reset, you can safely ignore this email. Your account will remain secure.<br>"
                    + "Need help? Contact our <a href='https://example.com/support' style='color:#0d6efd;text-decoration:underline;'>Support Center</a>.</p>"
                    + "</td></tr>"
                    + "<tr><td align='center' style='padding:16px 12px 22px;font-size:12px;color:#999999;'>"
                    + "Thank you for using our service.<br>"
                    + "<a href='https://example.com/privacy' style='color:#999999;text-decoration:underline;'>Privacy Policy</a> • "
                    + "<a href='https://example.com/unsubscribe' style='color:#999999;text-decoration:underline;'>Unsubscribe</a><br>"
                    + "© 2025 Project Name, 123 Sample Street, Ho Chi Minh City"
                    + "</td></tr></table>"
                    + "<table width='600' style='width:100%;max-width:600px;margin-top:8px;'><tr><td style='font-size:12px;color:#999999;text-align:center;'>"
                    + "Can’t click the button? Copy and paste this link into your browser:<br>"
                    + "<span style='word-break:break-all;'>" + resetUrl + "</span>"
                    + "</td></tr></table>"
                    + "</td></tr></table>"
                    + "</body></html>";

            body.put("html", html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_KEY);

            String jsonBody = mapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);
            System.out.println("Email sent successfully. Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
