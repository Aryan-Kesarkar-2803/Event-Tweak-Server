package com.example.EventTweak.controller;

import com.example.EventTweak.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String senEmail(){
        emailService.sendEmail(
                "johnSliver0033@gmail.com",
                "Email from Java",
                "This is test email"
        );
        return "Email sent successfully;";
    }
}
