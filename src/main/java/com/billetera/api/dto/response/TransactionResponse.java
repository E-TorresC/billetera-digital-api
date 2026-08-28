package com.billetera.api.dto.response;

import com.billetera.api.domain.enums.TransactionStatus;
import com.billetera.api.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long originWalletId;
    private Long destinationWalletId;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private String reference;
    private String idempotencyKey;
    private LocalDateTime createdAt;
}