package com.mangoApp.mangoBackend.marketplace.ordenes.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getProductPrice(Long productId) {
        String sql = "SELECT base_price_per_unit FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, productId);
    }

    public void createOrderAndUpdateStock(Long productId, Long buyerTenantId, int quantity, BigDecimal totalPrice) {
        // 1. Reducir stock (Solo si hay suficiente, el WHERE stock_available >= ? nos protege a nivel base de datos)
        String updateStockSql = "UPDATE products SET stock_available = stock_available - ? WHERE id = ? AND stock_available >= ?";
        int updatedRows = jdbcTemplate.update(updateStockSql, quantity, productId, quantity);

        if (updatedRows == 0) {
            throw new RuntimeException("Stock insuficiente para la cantidad solicitada");
        }

        // 2. Crear la orden en estado Escrow (Esto es lo que GraphHopper luego buscará)
        String insertOrderSql = "INSERT INTO orders (product_id, buyer_tenant_id, quantity_units, total_price, status) VALUES (?, ?, ?, ?, 'PAID_ESCROW')";
        jdbcTemplate.update(insertOrderSql, productId, buyerTenantId, quantity, totalPrice);
    }
}