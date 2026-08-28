package com.billetera.api.service;

import com.billetera.api.dto.request.TransferRequest;
import com.billetera.api.dto.response.TransactionResponse;

public interface TransferService {
    TransactionResponse transfer(TransferRequest request);
}