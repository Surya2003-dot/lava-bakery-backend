package com.lava.bakery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("surya.developer15@gmail.com");
        message.setTo(email);
        message.setSubject("Lava Bakery OTP Verification");

        message.setText(
                "Your OTP is: " + otp +
                        "\nValid for 5 minutes"
        );

        mailSender.send(message); // ❗ remove try-catch temporarily
    }
}