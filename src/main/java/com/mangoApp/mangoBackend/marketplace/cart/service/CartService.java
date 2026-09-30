package com.mangoApp.mangoBackend.marketplace.cart.service;

import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import com.mangoApp.mangoBackend.marketplace.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Map<String, Object>> getMyCart() {
        Long tenantId = SecurityUtils.getCurrentTenantId();
        return cartRepository.getCartByTenant(tenantId);
    }

    public void addItem(Long productId, Integer quantity) {
        Long tenantId = SecurityUtils.getCurrentTenantId();
        cartRepository.upsertCartItem(tenantId, productId, quantity);
    }

    public void removeItem(Long productId) {
        Long tenantId = SecurityUtils.getCurrentTenantId();
        cartRepository.removeCartItem(tenantId, productId);
    }
    
    public void clearCart() {
        Long tenantId = SecurityUtils.getCurrentTenantId();
        cartRepository.clearCart(tenantId);
    }
}
