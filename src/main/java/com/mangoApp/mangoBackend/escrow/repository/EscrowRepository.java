package com.mangoApp.mangoBackend.escrow.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EscrowRepository {

    private final JdbcTemplate jdbcTemplate;

    public EscrowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int markOrderAsCompleted(Long orderId) {
        String sql = "UPDATE orders SET status = 'COMPLETED' WHERE id = ? AND status = 'IN_TRANSIT'";
        return jdbcTemplate.update(sql, orderId);
    }

    public void markVehicleAsAvailable() {
        // En un MVP liberamos todos; en un entorno real recibiría el vehicleId
        String sql = "UPDATE vehicles SET is_available = 1";
        jdbcTemplate.update(sql);
    }
}