package com.mangoApp.mangoBackend.rutas.service;

import com.mangoApp.mangoBackend.rutas.repository.TrackingRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public TrackingService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    public void pingLocation(Long vehicleId, BigDecimal lat, BigDecimal lng) {
        // Aquí en el futuro puedes agregar validaciones (ej. que el chofer sea el dueño del vehículo)
        trackingRepository.updateVehicleLocation(vehicleId, lat, lng);
    }
}