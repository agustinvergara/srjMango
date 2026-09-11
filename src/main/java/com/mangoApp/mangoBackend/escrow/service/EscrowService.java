package com.mangoApp.mangoBackend.escrow.service;

import com.mangoApp.mangoBackend.escrow.model.WalletBalanceResponse;
import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class EscrowService {

    // Aquí inyectarías EscrowTransactionRepository y WalletRepository

    public WalletBalanceResponse getMyBalance() {
        Long currentTenantId = SecurityUtils.getCurrentTenantId();
        // Lógica DB: Sumar balances
        return new WalletBalanceResponse(currentTenantId, new BigDecimal("1000.00"), new BigDecimal("250.00"));
    }

    @Transactional
    public void releasePayment(Long orderId, String releasePin) {
        // 1. Buscar la transacción en escrow_transactions por orderId
        // 2. Validar que el PIN coincida (Si falla, lanzar excepción 400 Bad Request)
        // 3. Cambiar el estado a 'RELEASED'
        // 4. (Futuro) Disparar evento para transferir saldo real o token on-chain al productor y transportista
    }
}