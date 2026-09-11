package com.mangoApp.mangoBackend.marketplace.perecederos.repository;

import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;
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
}