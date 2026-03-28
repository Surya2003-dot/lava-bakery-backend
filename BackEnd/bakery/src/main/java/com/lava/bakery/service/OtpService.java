package com.lava.bakery.service;

import com.lava.bakery.entity.OtpVerification;
import com.lava.bakery.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public void sendOtp(String email){

        String otp = String.valueOf(
                (int)(Math.random()*900000)+100000
        );

        OtpVerification otpEntity = new OtpVerification();

        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );

        otpRepository.save(otpEntity);

        emailService.sendOtp(email,otp);
    }

}