package com.mangoApp.mangoBackend.escrow.service;

import com.mangoApp.mangoBackend.escrow.model.WalletBalanceResponse;
import com.mangoApp.mangoBackend.escrow.repository.EscrowRepository;
import com.mangoApp.mangoBackend.iam.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class EscrowService {

    private final EscrowRepository escrowRepository;

    public EscrowService(EscrowRepository escrowRepository) {
        this.escrowRepository = escrowRepository;
    }

    public WalletBalanceResponse getMyBalance() {
        Long currentTenantId = SecurityUtils.getCurrentTenantId();
        // Lógica DB: Sumar balances
        return new WalletBalanceResponse(currentTenantId, new BigDecimal("1000.00"), new BigDecimal("250.00"));
    }

    @Transactional
    public void releasePayment(Long orderId, String releasePin) {
        // 1. Validar que el PIN coincida
        if (!"1234".equals(releasePin)) {
            throw new RuntimeException("PIN de liberación inválido");
        }

        // 2. Cambiar el estado a 'COMPLETED'
        int rows = escrowRepository.markOrderAsCompleted(orderId);
        if (rows == 0) {
            throw new RuntimeException("La orden no existe o no está en tránsito");
        }
        
        // 3. Liberar el camión
        escrowRepository.markVehicleAsAvailable();
    }
}