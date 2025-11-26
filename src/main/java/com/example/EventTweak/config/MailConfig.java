package com.example.EventTweak.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

//    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public JavaMailSender mailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
//        mailSender.setUsername(dotenv.get("EMAIL_USER")); // or @Value
        mailSender.setUsername(System.getenv("EMAIL_USER")); // or @Value
        mailSender.setPassword(System.getenv("EMAIL_PASS"));
//        mailSender.setPassword(dotenv.get("EMAIL_PASS"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        return mailSender;
    }
}
