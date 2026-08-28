package com.billetera.api.dto.response;

import com.billetera.api.domain.enums.WalletStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WalletResponse {
    private Long id;
    private Long userId;
    private BigDecimal balance;
    private String currency;
    private WalletStatus status;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}