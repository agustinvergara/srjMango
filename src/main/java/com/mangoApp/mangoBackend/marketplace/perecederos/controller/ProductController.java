package com.mangoApp.mangoBackend.marketplace.perecederos.controller;

import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;
import com.mangoApp.mangoBackend.marketplace.perecederos.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marketplace/perecederos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/publish")
    public ResponseEntity<Void> publishProduct(@RequestBody Product product) {
        productService.publishProduct(product);
        return ResponseEntity.ok().build();
    }
}