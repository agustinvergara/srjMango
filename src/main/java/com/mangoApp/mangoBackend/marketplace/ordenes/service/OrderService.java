package com.mangoApp.mangoBackend.marketplace.ordenes.service;

import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import com.mangoApp.mangoBackend.marketplace.ordenes.model.dto.BuyProductRequest;
import com.mangoApp.mangoBackend.marketplace.ordenes.repository.OrderRepository;
import com.mangoApp.mangoBackend.marketplace.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public void processPurchase(BuyProductRequest request) {
        Long buyerTenantId = SecurityUtils.getCurrentTenantId();
        processSingleItem(buyerTenantId, request.productId(), request.quantity());
    }

    @Transactional
    public void checkoutCart() {
        Long buyerTenantId = SecurityUtils.getCurrentTenantId();
        
        var items = cartRepository.getCartByTenant(buyerTenantId);
        
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        for (var item : items) {
            Long productId = ((Number) item.get("productId")).longValue();
            Integer quantity = ((Number) item.get("quantity")).intValue();
            processSingleItem(buyerTenantId, productId, quantity);
        }

        cartRepository.clearCart(buyerTenantId);
    }

    private void processSingleItem(Long buyerTenantId, Long productId, Integer quantity) {
        BigDecimal basePrice = orderRepository.getProductPrice(productId);
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(quantity));

        orderRepository.createOrderAndUpdateStock(
            productId, 
            buyerTenantId, 
            quantity, 
            totalPrice
        );
    }
}
