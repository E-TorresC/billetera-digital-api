package com.billetera.api.service;

import com.billetera.api.dto.request.ChangeWalletStatusRequest;
import com.billetera.api.dto.request.CreateWalletRequest;
import com.billetera.api.dto.response.BalanceResponse;
import com.billetera.api.dto.response.WalletResponse;

import java.util.List;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);
    WalletResponse getWalletById(Long id);
    List<WalletResponse> getWalletsByUserId(Long userId);
    BalanceResponse getBalance(Long walletId);
    WalletResponse changeWalletStatus(Long id, ChangeWalletStatusRequest request);
}