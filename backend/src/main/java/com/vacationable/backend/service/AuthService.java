package com.vacationable.backend.service;

import com.vacationable.backend.dto.AuthResponse;
import com.vacationable.backend.dto.LoginRequest;
import com.vacationable.backend.dto.RegisterRequest;
import com.vacationable.backend.entity.User;
import com.vacationable.backend.exceptions.UserException;
import com.vacationable.backend.repository.UserRepository;
import com.vacationable.backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ADD THIS
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("Email already exists.");
        }

        // Create User entity from DTO
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // Hash it!
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        // Save entity
        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, "User registered successfully");
    }

    public AuthResponse login(LoginRequest request){
        // Check if email exists
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()
                -> new UserException("User not found"));

        // Check if password matches
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UserException("Incorrect password.");
        }

        // Generate token
        String token = jwtUtil.generateToken(request.getEmail());

        return new AuthResponse(token, "Logged in successfully");
    }

}
