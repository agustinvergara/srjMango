package com.mangoApp.mangoBackend.iam.model;
import java.time.LocalDateTime;

public record Tenant(
		Long id,
		String name,
		String businessType,
	    String ruc,
	    Boolean isActive,
	    LocalDateTime createdAt
	    ) {}
