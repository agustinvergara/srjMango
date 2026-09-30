package com.mangoApp.mangoBackend.marketplace.cart.model;

public record CartRequest(
    Long productId,
    Integer quantity
) {}
