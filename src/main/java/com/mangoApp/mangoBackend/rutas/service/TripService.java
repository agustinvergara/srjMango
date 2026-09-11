package com.mangoApp.mangoBackend.rutas.service;

import com.mangoApp.mangoBackend.rutas.model.AvailableTripResponse;
import com.mangoApp.mangoBackend.rutas.model.dto.PendingTripDTO;
import com.mangoApp.mangoBackend.rutas.model.RouteResult;
import com.mangoApp.mangoBackend.rutas.repository.RoutingRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TripService {

    private final RoutingEngineService routingEngineService;
    private final RoutingRepository routingRepository;

    public TripService(RoutingEngineService routingEngineService, RoutingRepository routingRepository) {
        this.routingEngineService = routingEngineService;
        this.routingRepository = routingRepository;
    }

    public List<AvailableTripResponse> findTripsForDriver(BigDecimal driverLat, BigDecimal driverLng) {
        List<AvailableTripResponse> trips = new ArrayList<>();
        
        // 1. Traer todas las órdenes listas para despachar de la BD real
        List<PendingTripDTO> pendingOrders = routingRepository.findPendingOrders();
        
        // 2. Procesar cada orden contra GraphHopper
        for (PendingTripDTO order : pendingOrders) {
            RouteResult route = routingEngineService.calculateRoute(
                driverLat.doubleValue(), driverLng.doubleValue(), 
                order.pickupLat().doubleValue(), order.pickupLng().doubleValue()
            );
            
            // 3. Regla: Mostrar viajes a máximo 50 km de distancia de manejo
            if (route.distanceKm() <= 50.0) {
                BigDecimal payout = BigDecimal.valueOf(route.distanceKm() * 1.50);
                
                trips.add(new AvailableTripResponse(
                        order.orderId(),
                        order.producerName(),
                        order.dropoffName(),
                        order.units(),
                        payout,
                        Math.round(route.distanceKm() * 100.0) / 100.0,
                        order.pickupLat(),   
                        order.pickupLng(),   
                        order.dropoffLat(),  
                        order.dropoffLng()   
                    ));
            }
        }
        
        return trips;
    }
    
    public void acceptTrip(Long tripId, Long vehicleId) {
        // Aquí podrías validar que la orden siga en PAID_ESCROW por si otro chofer la tomó 1 segundo antes
        routingRepository.assignTripToVehicle(tripId, vehicleId);
    }
}