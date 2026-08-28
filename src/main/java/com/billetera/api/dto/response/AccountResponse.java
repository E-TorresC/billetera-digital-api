package com.billetera.api.dto.response;

import com.billetera.api.domain.enums.AccountStatus;
import com.billetera.api.domain.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private Long userId;
    private String accountNumber;
    private AccountType type;
    private AccountStatus status;
    private LocalDateTime createdAt;
}