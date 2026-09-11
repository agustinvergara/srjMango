package com.mangoApp.mangoBackend.rutas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class TrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    public TrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateVehicleLocation(Long vehicleId, BigDecimal lat, BigDecimal lng) {
        String sql = "UPDATE vehicles SET current_lat = ?, current_lng = ?, last_location_update = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, lat, lng, vehicleId);
    }
}