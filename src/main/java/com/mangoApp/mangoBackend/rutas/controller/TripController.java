package com.mangoApp.mangoBackend.rutas.controller;

import com.mangoApp.mangoBackend.rutas.model.AvailableTripResponse;
import com.mangoApp.mangoBackend.rutas.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/logistica/viajes")
public class TripController {

	private final TripService tripService;

	public TripController(TripService tripService) {
        this.tripService = tripService;
    }
	
    @GetMapping("/cercanos")
    public ResponseEntity<List<AvailableTripResponse>> getNearbyTrips(
            @RequestParam BigDecimal currentLat,
            @RequestParam BigDecimal currentLng) {
        
        List<AvailableTripResponse> availableTrips = tripService.findTripsForDriver(currentLat, currentLng);
        return ResponseEntity.ok(availableTrips);
    }

    @PostMapping("/{tripId}/aceptar")
    public ResponseEntity<Void> acceptTrip(@PathVariable Long tripId) {
        // Lógica: Cambiar estado del trip a 'ASSIGNED' y marcar vehículo como no disponible
        return ResponseEntity.ok().build();
    }
    
    
}