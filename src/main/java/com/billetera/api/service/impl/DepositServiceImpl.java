package com.billetera.api.service.impl;

import com.billetera.api.domain.enums.MovementType;
import com.billetera.api.domain.enums.TransactionStatus;
import com.billetera.api.domain.enums.TransactionType;
import com.billetera.api.domain.model.Movement;
import com.billetera.api.domain.model.Transaction;
import com.billetera.api.domain.model.Wallet;
import com.billetera.api.dto.request.DepositRequest;
import com.billetera.api.dto.response.TransactionResponse;
import com.billetera.api.exception.BusinessException;
import com.billetera.api.exception.ResourceNotFoundException;
import com.billetera.api.repository.MovementRepository;
import com.billetera.api.repository.TransactionRepository;
import com.billetera.api.repository.WalletRepository;
import com.billetera.api.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final MovementRepository movementRepository;

    @Override
    @Transactional
    public TransactionResponse deposit(Long walletId, DepositRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada con ID: " + walletId));

        if (!wallet.isActive()) {
            throw new BusinessException("No se pueden realizar depósitos en una billetera inactiva o bloqueada");
        }

        BigDecimal previousBalance = wallet.getBalance();

        // 1. Aplicar crédito al saldo de la billetera (Lógica de Dominio)
        wallet.credit(request.getAmount());
        walletRepository.save(wallet);

        // 2. Registrar Transacción
        Transaction transaction = Transaction.builder()
                .destinationWallet(wallet)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .currency(wallet.getCurrency())
                .status(TransactionStatus.COMPLETED)
                .reference(request.getReference() != null ? request.getReference() : "Depósito en cuenta")
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 3. Registrar Movimiento Contable (Entrada)
        Movement movement = Movement.builder()
                .wallet(wallet)
                .transaction(savedTransaction)
                .type(MovementType.IN)
                .amount(request.getAmount())
                .previousBalance(previousBalance)
                .postBalance(wallet.getBalance())
                .build();
        movementRepository.save(movement);

        return mapToTransactionResponse(savedTransaction);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .originWalletId(transaction.getOriginWallet() != null ? transaction.getOriginWallet().getId() : null)
                .destinationWalletId(transaction.getDestinationWallet() != null ? transaction.getDestinationWallet().getId() : null)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .reference(transaction.getReference())
                .idempotencyKey(transaction.getIdempotencyKey())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}