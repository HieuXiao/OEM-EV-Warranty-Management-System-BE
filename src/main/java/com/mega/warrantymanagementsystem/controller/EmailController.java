package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.EmailDetail;
import com.mega.warrantymanagementsystem.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin
@SecurityRequirement(name = "api")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestBody EmailDetail emailDetail){
        emailService.sendMailTemplate(emailDetail, "campaign-template.html");
    }

}
