package com.billetera.api.controller;

import com.billetera.api.dto.request.DepositRequest;
import com.billetera.api.dto.response.TransactionResponse;
import com.billetera.api.service.DepositService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets/{walletId}/depositos")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long walletId,
                                                       @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(depositService.deposit(walletId, request));
    }
}