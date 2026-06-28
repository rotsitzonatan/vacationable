package com.vacationable.backend.controller;

import com.vacationable.backend.dto.AuthResponse;
import com.vacationable.backend.dto.LoginRequest;
import com.vacationable.backend.dto.RegisterRequest;
import com.vacationable.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
           @Valid @RequestBody RegisterRequest registerRequest){
        AuthResponse auth = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(auth);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest){
        AuthResponse auth = authService.login(loginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(auth);
    }
}
