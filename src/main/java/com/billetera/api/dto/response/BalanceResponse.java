package com.billetera.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceResponse {
    private Long walletId;
    private BigDecimal balance;
    private String currency;
}