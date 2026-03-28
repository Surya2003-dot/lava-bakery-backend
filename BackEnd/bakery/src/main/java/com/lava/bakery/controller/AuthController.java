package com.lava.bakery.controller;

import com.lava.bakery.dto.LoginRequest;
import com.lava.bakery.dto.LoginResponse;
import com.lava.bakery.dto.RegisterRequest;
import com.lava.bakery.dto.ResetPasswordRequest;
import com.lava.bakery.entity.OtpVerification;
import com.lava.bakery.entity.User;
import com.lava.bakery.repository.OtpRepository;
import com.lava.bakery.repository.UserRepository;
import com.lava.bakery.service.OtpService;
import com.lava.bakery.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserService userService,
            OtpService otpService,
            OtpRepository otpRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.otpService = otpService;
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        OtpVerification otpRecord = otpRepository
                .findTopByEmailOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!request.getOtp().equals(otpRecord.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        userService.register(request);

        return "Account created successfully";

    }


    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().getName().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Access Denied: Not Admin");
        }

        return ResponseEntity.ok(response);
    }
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().getName().equals("USER")) {
            return ResponseEntity.status(403).body("Access Denied: Not User");
        }

        return ResponseEntity.ok(response);
    }
    // TEST SECURE
    @GetMapping("/secure")
    public String secure() {
        return "You accessed secured endpoint!";
    }

    @PostMapping("/send-register-otp")
    public String sendRegisterOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists";
        }

        otpService.sendOtp(email);

        return "OTP sent successfully";
    }

    @PostMapping("/send-forgot-otp")
    public String sendForgotOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        if (userRepository.findByEmail(email).isEmpty()) {
            return "Email not registered";
        }

        otpService.sendOtp(email);

        return "OTP sent successfully";
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();

        Optional<OtpVerification> otpOpt =
                otpRepository.findTopByEmailOrderByIdDesc(email);

        if (otpOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("OTP not found");
        }

        OtpVerification otpRecord = otpOpt.get();

        if (!otp.equals(otpRecord.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        if (otpRecord.getExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        otpRepository.delete(otpRecord);

        return ResponseEntity.ok("Password reset successful");

    }
}