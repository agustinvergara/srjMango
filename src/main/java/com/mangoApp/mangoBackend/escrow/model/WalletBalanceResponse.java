package com.mangoApp.mangoBackend.escrow.model;

import java.math.BigDecimal;

//Respuesta para la vista de "Mi Billetera" del Productor o Comprador
public record WalletBalanceResponse(
 Long tenantId,
 BigDecimal availableBalance,
 BigDecimal heldInEscrow
) {}