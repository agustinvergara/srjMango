package com.mangoApp.mangoBackend.rutas.repository;

import com.mangoApp.mangoBackend.rutas.model.dto.PendingTripDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class RoutingRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoutingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Busca camiones disponibles en un radio X con espacio suficiente
    public List<Long> findAvailableVehiclesNearby(
            BigDecimal originLng, 
            BigDecimal originLat, 
            Integer requiredUnits, 
            Integer radiusKm) {
        
        String sql = """
            SELECT id 
            FROM vehicles 
            WHERE is_available = true 
              AND capacity_in_units >= ? 
              AND ST_Distance_Sphere(POINT(?, ?), POINT(current_lng, current_lat)) <= ?
        """;
        
        // Multiplicamos radiusKm por 1000 porque MySQL calcula en metros
        return jdbcTemplate.queryForList(sql, Long.class, 
            requiredUnits, originLng, originLat, radiusKm * 1000);
    }
    
    public List<PendingTripDTO> findPendingOrders() {
        String sql = """
            SELECT o.id, t_prod.name as producer_name, t_buyer.name as buyer_name, 
                   o.quantity_units, 
                   t_prod.lat as pickup_lat, t_prod.lng as pickup_lng,
                   t_buyer.lat as dropoff_lat, t_buyer.lng as dropoff_lng
            FROM orders o
            JOIN products p ON o.product_id = p.id
            JOIN tenants t_prod ON p.tenant_id = t_prod.id
            JOIN tenants t_buyer ON o.buyer_tenant_id = t_buyer.id
            WHERE o.status = 'PAID_ESCROW'
              AND t_prod.lat IS NOT NULL
              AND t_buyer.lat IS NOT NULL
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PendingTripDTO(
            rs.getLong("id"),
            rs.getString("producer_name"),
            rs.getString("buyer_name"),
            rs.getInt("quantity_units"),
            rs.getBigDecimal("pickup_lat"),
            rs.getBigDecimal("pickup_lng"),
            rs.getBigDecimal("dropoff_lat"), // <-- NUEVO
            rs.getBigDecimal("dropoff_lng")  // <-- NUEVO
        ));
    }
}