package com.mangoApp.mangoBackend.iam.controller;

import com.mangoApp.mangoBackend.iam.model.dto.AuthResponse;
import com.mangoApp.mangoBackend.iam.model.dto.LoginRequest;
import com.mangoApp.mangoBackend.iam.model.dto.RegisterRequest;
import com.mangoApp.mangoBackend.iam.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

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
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_" + UUID.randomUUID().toString().substring(0,8);
        return ResponseEntity.ok(new AuthResponse(
            mockToken, 
            1L, 
            "TRANSPORTISTA"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            String[] result = authService.registerUser(request);
            String token = result[0];
            Long tenantId = Long.parseLong(result[1]);
            String role = result[2];
            return ResponseEntity.ok(new AuthResponse(token, tenantId, role));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
