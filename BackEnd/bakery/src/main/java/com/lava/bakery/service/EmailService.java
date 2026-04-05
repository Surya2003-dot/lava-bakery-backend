package com.lava.bakery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Lava Bakery OTP Verification");
        message.setText("Your OTP is: " + otp + "\nValid for 5 minutes");

        mailSender.send(message);  // ✅ No try/catch — throws if fails

        System.out.println("✅ MAIL SENT SUCCESS to: " + email);
    }
}