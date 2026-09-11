package com.mangoApp.mangoBackend.marketplace.logistica.controller;

import com.mangoApp.mangoBackend.marketplace.logistica.model.Vehicle;
import com.mangoApp.mangoBackend.marketplace.logistica.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marketplace/logistica/vehiculos")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.registerVehicle(vehicle);
        return ResponseEntity.ok().build();
    }
}