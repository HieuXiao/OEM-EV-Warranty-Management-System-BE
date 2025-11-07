package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender mailSender;


    public void sendMailTemplate(EmailDetail emailDetail, String template){
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getFullName());
            context.setVariable("url", emailDetail.getUrl());
            context.setVariable("campaign", emailDetail.getCampaignName());
            context.setVariable("model", emailDetail.getModelName());
            context.setVariable("startDate", emailDetail.getStartDate());
            context.setVariable("endDate", emailDetail.getEndDate());

            String text = templateEngine.process(template, context);

            // creating a simple mail message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);


            // setting up necessary details
            mimeMessageHelper.setFrom("admin@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text , true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mailSender.send(mimeMessage);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
