package com.mangoApp.mangoBackend.iam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            
            // Lógica MVP: Aquí iría el parseo real con io.jsonwebtoken.
            // Para poder avanzar sin frenarnos, mockeamos la extracción:
            Long tenantId = 1L; 
            String email = "productor@finca.com"; 

            UserPrincipal principal = new UserPrincipal(1L, tenantId, email, "", Collections.emptyList());
            var authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        
        filterChain.doFilter(request, response);
    }
}
