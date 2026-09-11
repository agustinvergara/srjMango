package com.mangoApp.mangoBackend.marketplace.logistica.service;

import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import com.mangoApp.mangoBackend.marketplace.logistica.model.Vehicle;
import com.mangoApp.mangoBackend.marketplace.logistica.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void registerVehicle(Vehicle requestDto) {
        Long currentTenantId = SecurityUtils.getCurrentTenantId();

        // Regla de negocio simple para evitar fraudes de capacidad
        if (requestDto.vehicleType().equals("PICKUP") && requestDto.capacityInUnits() > 50) {
            throw new IllegalArgumentException("Un pickup no puede llevar más de 50 canastillas");
        }

        Vehicle vehicleToSave = new Vehicle(
            null,
            currentTenantId,
            requestDto.plateNumber(),
            requestDto.vehicleType(),
            requestDto.capacityInUnits(),
            true, // Siempre disponible al registrarse
            null, null, null // Ubicación inicial nula hasta que prendan la app
        );

        vehicleRepository.save(vehicleToSave);
    }
}