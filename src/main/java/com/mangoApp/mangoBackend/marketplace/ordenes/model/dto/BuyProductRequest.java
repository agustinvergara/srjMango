package com.mangoApp.mangoBackend.marketplace.ordenes.model.dto;

public record BuyProductRequest(
		Long productId, 
	    Integer quantity
	    ) {

}
