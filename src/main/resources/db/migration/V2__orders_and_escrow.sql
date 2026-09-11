-- Gestión de la compra
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    buyer_tenant_id BIGINT NOT NULL,
    quantity_units INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING_PAYMENT', 'PAID_ESCROW', 'IN_TRANSIT', 'DELIVERED') DEFAULT 'PENDING_PAYMENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_orders_buyer FOREIGN KEY (buyer_tenant_id) REFERENCES tenants(id)
);

-- Bóveda de retención de fondos (Escrow)
CREATE TABLE escrow_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    release_pin VARCHAR(6) NOT NULL, -- El código que el comprador da al chofer
    status ENUM('HELD', 'RELEASED', 'REFUNDED') DEFAULT 'HELD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_escrow_order FOREIGN KEY (order_id) REFERENCES orders(id)
);