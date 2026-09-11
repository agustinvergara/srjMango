package com.mangoApp.mangoBackend.marketplace.perecederos.repository;

import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Product product) {
        String sql = """
            INSERT INTO products 
            (tenant_id, name, category, requires_refrigeration, base_price_per_unit, unit_type, stock_available)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        jdbcTemplate.update(sql,
            product.tenantId(),
            product.name(),
            product.category(),
            product.requiresRefrigeration(),
            product.basePricePerUnit(),
            product.unitType(),
            product.stockAvailable()
        );
    }
    
    public List<Map<String, Object>> findAllAvailable() {
        // Usamos un Map para evitar crear un DTO complejo solo para listar en el MVP.
        // Hacemos JOIN con tenants para que el frontend pueda mostrar quién lo vende.
        String sql = """
            SELECT p.id, p.name, p.category, p.base_price_per_unit, p.stock_available, 
                   t.name as producer_name 
            FROM products p 
            JOIN tenants t ON p.tenant_id = t.id 
            WHERE p.stock_available > 0
        """;
        return jdbcTemplate.queryForList(sql);
    }
}