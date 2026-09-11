package com.mangoApp.mangoBackend.rutas.model.dto;

import java.math.BigDecimal;

public record PendingTripDTO(
    Long orderId, 
    String producerName, 
    String dropoffName, 
    Integer units, 
    BigDecimal pickupLat, 
    BigDecimal pickupLng
) {}