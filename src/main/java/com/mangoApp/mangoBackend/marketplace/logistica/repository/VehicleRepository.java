package com.mangoApp.mangoBackend.marketplace.logistica.repository;

import com.mangoApp.mangoBackend.marketplace.logistica.model.Vehicle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleRepository {

    private final JdbcTemplate jdbcTemplate;

    public VehicleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Vehicle vehicle) {
        String sql = """
            INSERT INTO vehicles 
            (tenant_id, plate_number, vehicle_type, capacity_in_units, is_available)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        jdbcTemplate.update(sql,
            vehicle.tenantId(),
            vehicle.plateNumber(),
            vehicle.vehicleType(),
            vehicle.capacityInUnits(),
            vehicle.isAvailable() != null ? vehicle.isAvailable() : true
        );
    }
}