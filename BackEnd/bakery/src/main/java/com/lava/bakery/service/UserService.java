package com.lava.bakery.service;

import com.lava.bakery.dto.LoginRequest;
import com.lava.bakery.dto.LoginResponse;
import com.lava.bakery.dto.RegisterRequest;
import com.lava.bakery.entity.Role;
import com.lava.bakery.entity.User;
import com.lava.bakery.repository.RoleRepository;
import com.lava.bakery.repository.UserRepository;
import com.lava.bakery.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Constructor Injection
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    //  REGISTER METHOD
    public String register(RegisterRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already exists";
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User(
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                userRole,
                null
        );

        userRepository.save(user);

        return "User registered successfully!";
    }
    //  LOGIN METHOD
    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getName()
        );

        // Return JSON response
        return new LoginResponse("Login successful", token);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}