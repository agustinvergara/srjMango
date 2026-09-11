package com.mangoApp.mangoBackend.marketplace.perecederos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(
		Long id,
	    Long productId,
	    Long buyerTenantId,
	    Integer quantityUnits,
	    BigDecimal totalPrice,
	    String status,
	    LocalDateTime createdAt
	    ) {

}
