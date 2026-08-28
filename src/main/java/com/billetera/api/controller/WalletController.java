package com.billetera.api.controller;

import com.billetera.api.dto.request.ChangeWalletStatusRequest;
import com.billetera.api.dto.request.CreateWalletRequest;
import com.billetera.api.dto.response.BalanceResponse;
import com.billetera.api.dto.response.WalletResponse;
import com.billetera.api.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getWalletById(id));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<WalletResponse>> getWalletsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWalletsByUserId(userId));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.getBalance(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<WalletResponse> changeWalletStatus(@PathVariable Long id,
                                                             @Valid @RequestBody ChangeWalletStatusRequest request) {
        return ResponseEntity.ok(walletService.changeWalletStatus(id, request));
    }
}