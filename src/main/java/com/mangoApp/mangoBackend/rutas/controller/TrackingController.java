package com.mangoApp.mangoBackend.rutas.controller;

import com.mangoApp.mangoBackend.rutas.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/logistica/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PutMapping("/vehiculo/{vehicleId}")
    public ResponseEntity<Void> updateLocation(
            @PathVariable Long vehicleId, 
            @RequestParam BigDecimal lat, 
            @RequestParam BigDecimal lng) {
        
        trackingService.pingLocation(vehicleId, lat, lng);
        return ResponseEntity.ok().build();
    }
}