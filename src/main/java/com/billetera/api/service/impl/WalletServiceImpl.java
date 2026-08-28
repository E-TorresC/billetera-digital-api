package com.billetera.api.service.impl;

import com.billetera.api.domain.enums.WalletStatus;
import com.billetera.api.domain.model.User;
import com.billetera.api.domain.model.Wallet;
import com.billetera.api.dto.request.ChangeWalletStatusRequest;
import com.billetera.api.dto.request.CreateWalletRequest;
import com.billetera.api.dto.response.BalanceResponse;
import com.billetera.api.dto.response.WalletResponse;
import com.billetera.api.exception.ResourceNotFoundException;
import com.billetera.api.repository.UserRepository;
import com.billetera.api.repository.WalletRepository;
import com.billetera.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public WalletResponse createWallet(CreateWalletRequest request) {
        User user = userRepository.findByIdAndStatusTrue(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo con ID: " + request.getUserId()));

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency(request.getCurrency().toUpperCase())
                .status(WalletStatus.ACTIVE)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);
        return mapToResponse(savedWallet);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada con ID: " + id));
        return mapToResponse(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WalletResponse> getWalletsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return walletRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada con ID: " + walletId));
        return BalanceResponse.builder()
                .walletId(wallet.getId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .build();
    }

    @Override
    @Transactional
    public WalletResponse changeWalletStatus(Long id, ChangeWalletStatusRequest request) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada con ID: " + id));

        wallet.setStatus(request.getStatus());
        Wallet updatedWallet = walletRepository.save(wallet);
        return mapToResponse(updatedWallet);
    }

    private WalletResponse mapToResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUser().getId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .status(wallet.getStatus())
                .version(wallet.getVersion())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }
}