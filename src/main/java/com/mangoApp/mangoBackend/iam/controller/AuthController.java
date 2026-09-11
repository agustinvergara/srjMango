package com.mangoApp.mangoBackend.iam.controller;

import com.mangoApp.mangoBackend.iam.model.dto.AuthResponse;
import com.mangoApp.mangoBackend.iam.model.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // En el futuro inyectaremos AuthService aquí para validar la BD y firmar un JWT real

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        
        // MVP: Validamos dummy y generamos un token falso para destrabar el frontend
        if (request.password() == null || request.password().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_" + UUID.randomUUID().toString().substring(0,8);
        
        return ResponseEntity.ok(new AuthResponse(
            mockToken, 
            1L, // tenantId de prueba
            "TRANSPORTISTA"
        ));
    }
}