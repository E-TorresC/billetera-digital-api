package com.billetera.api.controller;

import com.billetera.api.dto.request.WithdrawalRequest;
import com.billetera.api.dto.response.TransactionResponse;
import com.billetera.api.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets/{walletId}/retiros")
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @PostMapping
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long walletId,
                                                        @Valid @RequestBody WithdrawalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(withdrawalService.withdraw(walletId, request));
    }
}