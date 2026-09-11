package com.mangoApp.mangoBackend.marketplace.perecederos.service;

import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import com.mangoApp.mangoBackend.marketplace.perecederos.model.Product;
import com.mangoApp.mangoBackend.marketplace.perecederos.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void publishProduct(Product requestDto) {
        // 1. Obtenemos el Tenant ID del usuario autenticado (productor)
        Long currentTenantId = SecurityUtils.getCurrentTenantId();
        
        // 2. Construimos el modelo final forzando el tenant y las reglas de negocio
        Product productToSave = new Product(
            null, // El ID lo autogenera la BD
            currentTenantId,
            requestDto.name(),
            requestDto.category(),
            requestDto.requiresRefrigeration(),
            requestDto.basePricePerUnit(),
            requestDto.unitType() != null ? requestDto.unitType() : "CANASTILLA_20KG", // Valor por defecto
            requestDto.stockAvailable(),
            null // El timestamp lo autogenera la BD
        );
        
        // 3. Persistimos
        productRepository.save(productToSave);
    }
}