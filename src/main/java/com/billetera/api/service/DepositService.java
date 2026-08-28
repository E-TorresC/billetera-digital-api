package com.billetera.api.service;

import com.billetera.api.dto.request.DepositRequest;
import com.billetera.api.dto.response.TransactionResponse;

public interface DepositService {
    TransactionResponse deposit(Long walletId, DepositRequest request);
}