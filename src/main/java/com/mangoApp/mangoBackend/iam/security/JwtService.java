package com.mangoApp.mangoBackend.iam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    // Llave estática para desarrollo (evita que los tokens se invaliden al reiniciar el servidor)
    private static final String SECRET_STRING = "srjMangoSecretKeyForDevelopmentOnlyDoNotUseInProd1234567890";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 1 dia

    public String generateToken(Long tenantId, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tenantId", tenantId)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
