package com.mangoApp.mangoBackend.iam.controller;

import com.mangoApp.mangoBackend.iam.model.dto.AuthResponse;
import com.mangoApp.mangoBackend.iam.model.dto.LoginRequest;
import com.mangoApp.mangoBackend.iam.model.dto.RegisterRequest;
import com.mangoApp.mangoBackend.iam.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String[] result = authService.loginUser(request);
            return ResponseEntity.ok(new AuthResponse(result[0], Long.parseLong(result[1]), result[2]));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            String[] result = authService.registerUser(request);
            return ResponseEntity.ok(new AuthResponse(result[0], Long.parseLong(result[1]), result[2]));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
