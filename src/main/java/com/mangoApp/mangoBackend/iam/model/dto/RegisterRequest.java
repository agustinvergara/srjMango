package com.mangoApp.mangoBackend.iam.model.dto;

public record RegisterRequest(
    String role,
    String fullName,
    String email,
    String phone,
    String companyName,
    String taxId,
    String password,
    Profile profile
) {
    public record Profile(
        String farmName,
        String province,
        String crops,
        Boolean hasRefrigeration,
        String vehicleType,
        String plate,
        Integer capacityKg,
        String license,
        Boolean refrigerated
    ) {}
}
