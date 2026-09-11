package com.mangoApp.mangoBackend.marketplace.logistica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Vehicle(
    Long id,
    Long tenantId,
    String plateNumber,
    String vehicleType, // PICKUP, SMALL_TRUCK, REFRIGERATED_TRUCK
    Integer capacityInUnits, // Cuántas canastillas de 20kg le caben
    Boolean isAvailable,
    BigDecimal currentLat,
    BigDecimal currentLng,
    LocalDateTime lastLocationUpdate
) {}