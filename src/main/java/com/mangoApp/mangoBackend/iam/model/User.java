package com.mangoApp.mangoBackend.iam.model;

public record User(
		Long id,
	    Long tenantId,
	    String email,
	    String passwordHash,
	    String role,
	    Boolean isVerified
		) {

}
