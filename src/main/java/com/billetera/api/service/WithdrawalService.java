package com.billetera.api.service;

import com.billetera.api.dto.request.WithdrawalRequest;
import com.billetera.api.dto.response.TransactionResponse;

public interface WithdrawalService {
    TransactionResponse withdraw(Long walletId, WithdrawalRequest request);
}