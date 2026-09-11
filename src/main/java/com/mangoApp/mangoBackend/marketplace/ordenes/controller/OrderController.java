package com.mangoApp.mangoBackend.marketplace.ordenes.controller;

import com.mangoApp.mangoBackend.marketplace.ordenes.model.dto.BuyProductRequest;
import com.mangoApp.mangoBackend.marketplace.ordenes.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marketplace/ordenes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/comprar")
    public ResponseEntity<Void> buyProduct(@RequestBody BuyProductRequest request) {
        orderService.processPurchase(request);
        return ResponseEntity.ok().build();
    }
}