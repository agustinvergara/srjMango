package com.mangoApp.mangoBackend.escrow.controller;

import com.mangoApp.mangoBackend.escrow.model.ReleaseFundsRequest;
import com.mangoApp.mangoBackend.escrow.model.WalletBalanceResponse;
import com.mangoApp.mangoBackend.escrow.service.EscrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/escrow")
public class EscrowController {

    private final EscrowService escrowService;

    public EscrowController(EscrowService escrowService) {
        this.escrowService = escrowService;
    }

    // El frontend llama aquí para mostrar "Saldo Retenido: $250.00"
    @GetMapping("/balance")
    public ResponseEntity<WalletBalanceResponse> getBalance() {
        WalletBalanceResponse balance = escrowService.getMyBalance();
        return ResponseEntity.ok(balance);
    }

    // El frontend (App del Chofer) llama aquí cuando el comprador le da el PIN de la entrega
    @PostMapping("/release")
    public ResponseEntity<Void> releaseFunds(@RequestBody ReleaseFundsRequest request) {
        escrowService.releasePayment(request.orderId(), request.releasePin());
        return ResponseEntity.ok().build();
    }
}