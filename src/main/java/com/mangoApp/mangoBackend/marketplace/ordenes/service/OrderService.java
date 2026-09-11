package com.mangoApp.mangoBackend.marketplace.ordenes.service;

import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import com.mangoApp.mangoBackend.marketplace.ordenes.model.dto.BuyProductRequest;
import com.mangoApp.mangoBackend.marketplace.ordenes.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Usamos @Transactional para que, si algo falla (ej. se cae la BD al crear la orden), 
    // no se le reste el stock al productor por error (rollback automático).
    @Transactional
    public void processPurchase(BuyProductRequest request) {
        // 1. Identificamos quién está comprando desde el JWT (Ej: el Minisuper)
        Long buyerTenantId = SecurityUtils.getCurrentTenantId();

        // 2. Buscamos el precio real en BD y calculamos el total de forma segura
        BigDecimal basePrice = orderRepository.getProductPrice(request.productId());
        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(request.quantity()));

        // 3. Ejecutamos la reducción de inventario y la creación de la orden
        orderRepository.createOrderAndUpdateStock(
            request.productId(), 
            buyerTenantId, 
            request.quantity(), 
            totalPrice
        );
    }
}