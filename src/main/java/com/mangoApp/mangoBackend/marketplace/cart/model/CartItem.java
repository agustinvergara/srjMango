package com.mangoApp.mangoBackend.marketplace.cart.model;

import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;

public record CartItem(
    Long id,
    Long tenantId,
    Long productId,
    Integer quantity,
    Product product
) {}
