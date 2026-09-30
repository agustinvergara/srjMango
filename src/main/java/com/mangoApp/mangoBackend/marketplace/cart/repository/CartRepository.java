package com.mangoApp.mangoBackend.marketplace.cart.repository;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getCartByTenant(Long tenantId) {
        String sql = """
            SELECT c.id as "cartItemId", c.quantity, c.product_id as "productId",
                   p.name, p.category, p.base_price_per_unit as "basePricePerUnit",
                   p.stock_available as "stockAvailable", p.unit_type as "unit",
                   p.photo_urls as "photoUrls", p.requires_refrigeration as "requiresRefrigeration",
                   t.name as "producer"
            FROM cart_items c
            JOIN products p ON c.product_id = p.id
            JOIN tenants t ON p.tenant_id = t.id
            WHERE c.tenant_id = ?
        """;
        return jdbcTemplate.queryForList(sql, tenantId);
    }

    public void upsertCartItem(Long tenantId, Long productId, Integer quantity) {
        String sql = """
            INSERT INTO cart_items (tenant_id, product_id, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = ?
        """;
        jdbcTemplate.update(sql, tenantId, productId, quantity, quantity);
    }

    public void removeCartItem(Long tenantId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE tenant_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, tenantId, productId);
    }
    
    public void clearCart(Long tenantId) {
        String sql = "DELETE FROM cart_items WHERE tenant_id = ?";
        jdbcTemplate.update(sql, tenantId);
    }
}
