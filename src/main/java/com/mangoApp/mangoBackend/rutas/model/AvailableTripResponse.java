package com.mangoApp.mangoBackend.rutas.model;

import java.math.BigDecimal;

public record AvailableTripResponse(
    Long tripId,
    String pickupLocationName,
    String dropoffLocationName,
    Integer requiredCanastillas,
    BigDecimal estimatedPayout, // Lo que gana el chofer
    Double distanceToPickupKm
) {}