package com.mangoApp.mangoBackend.marketplace.perecederos.repository;

import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void save(Product product) {
        String sql = """
            INSERT INTO products 
            (tenant_id, name, category, requires_refrigeration, base_price_per_unit, unit_type, stock_available, description, expiration_date, condition_type, photo_urls)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        String photoUrlsJson = "[]";
        try {
            if (product.photoUrls() != null) {
                photoUrlsJson = objectMapper.writeValueAsString(product.photoUrls());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        jdbcTemplate.update(sql,
            product.tenantId(),
            product.name(),
            product.category(),
            product.requiresRefrigeration(),
            product.basePricePerUnit(),
            product.unitType(),
            product.stockAvailable(),
            product.description(),
            product.expirationDate(),
            product.conditionType(),
            photoUrlsJson
        );
    }
    
    public List<Map<String, Object>> findAllAvailable() {
        String sql = """
            SELECT p.id, p.name, p.category, p.base_price_per_unit, p.stock_available, 
                   p.unit_type, p.description, p.expiration_date, p.condition_type, p.photo_urls,
                   t.name as producer_name 
            FROM products p 
            JOIN tenants t ON p.tenant_id = t.id 
            WHERE p.stock_available > 0
        """;
        return jdbcTemplate.queryForList(sql);
    }
}