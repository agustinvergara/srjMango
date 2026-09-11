package com.mangoApp.mangoBackend.iam.model.dto;

public record AuthResponse(String token, Long tenantId, String role) {}