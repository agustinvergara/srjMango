package com.mangoApp.mangoBackend.marketplace.perecederos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
    Long id,
    Long tenantId,
    String name,
    String category,
    Boolean requiresRefrigeration,
    BigDecimal basePricePerUnit,
    String unitType,
    Integer stockAvailable,
    String description,
    java.time.LocalDate expirationDate,
    String conditionType,
    java.util.List<String> photoUrls,
    LocalDateTime createdAt
) {}