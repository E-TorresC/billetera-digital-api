package com.billetera.api.service.impl;

import com.billetera.api.domain.enums.MovementType;
import com.billetera.api.domain.enums.TransactionStatus;
import com.billetera.api.domain.enums.TransactionType;
import com.billetera.api.domain.model.Movement;
import com.billetera.api.domain.model.Transaction;
import com.billetera.api.domain.model.Wallet;
import com.billetera.api.dto.request.TransferRequest;
import com.billetera.api.dto.response.TransactionResponse;
import com.billetera.api.exception.BusinessException;
import com.billetera.api.exception.ResourceNotFoundException;
import com.billetera.api.repository.MovementRepository;
import com.billetera.api.repository.TransactionRepository;
import com.billetera.api.repository.WalletRepository;
import com.billetera.api.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final MovementRepository movementRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getOriginWalletId().equals(request.getDestinationWalletId())) {
            throw new BusinessException("No se pueden realizar transferencias a la misma billetera");
        }

        // 1. Prevenir Deadlocks: Cargar y bloquear las billeteras siguiendo un orden determinista por ID ASC
        List<Long> walletIds = List.of(request.getOriginWalletId(), request.getDestinationWalletId());
        List<Wallet> lockedWallets = walletRepository.findAllByIdsWithPessimisticLockOrdered(walletIds);

        if (lockedWallets.size() < 2) {
            throw new ResourceNotFoundException("Una o ambas billeteras no existen");
        }

        // Asignar origen y destino desde la lista bloqueada
        Wallet originWallet = lockedWallets.stream()
                .filter(w -> w.getId().equals(request.getOriginWalletId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Billetera de origen no encontrada con ID: " + request.getOriginWalletId()));

        Wallet destinationWallet = lockedWallets.stream()
                .filter(w -> w.getId().equals(request.getDestinationWalletId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Billetera de destino no encontrada con ID: " + request.getDestinationWalletId()));

        // Validaciones de estado
        if (!originWallet.isActive()) {
            throw new BusinessException("La billetera de origen no se encuentra activa");
        }
        if (!destinationWallet.isActive()) {
            throw new BusinessException("La billetera de destino no se encuentra activa");
        }
        if (!originWallet.getCurrency().equalsIgnoreCase(destinationWallet.getCurrency())) {
            throw new BusinessException("Las billeteras deben manejar la misma moneda para realizar transferencias");
        }

        BigDecimal originPreviousBalance = originWallet.getBalance();
        BigDecimal destPreviousBalance = destinationWallet.getBalance();

        // 2. Modificación de Saldos
        try {
            originWallet.debit(request.getAmount());
        } catch (IllegalStateException e) {
            throw new BusinessException(e.getMessage());
        }

        destinationWallet.credit(request.getAmount());

        walletRepository.save(originWallet);
        walletRepository.save(destinationWallet);

        // 3. Registrar Transacción Global
        Transaction transaction = Transaction.builder()
                .originWallet(originWallet)
                .destinationWallet(destinationWallet)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .currency(originWallet.getCurrency())
                .status(TransactionStatus.COMPLETED)
                .reference(request.getReference() != null ? request.getReference() : "Transferencia entre billeteras")
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 4. Registrar Movimientos
        Movement originMovement = Movement.builder()
                .wallet(originWallet)
                .transaction(savedTransaction)
                .type(MovementType.OUT)
                .amount(request.getAmount())
                .previousBalance(originPreviousBalance)
                .postBalance(originWallet.getBalance())
                .build();
        movementRepository.save(originMovement);

        Movement destinationMovement = Movement.builder()
                .wallet(destinationWallet)
                .transaction(savedTransaction)
                .type(MovementType.IN)
                .amount(request.getAmount())
                .previousBalance(destPreviousBalance)
                .postBalance(destinationWallet.getBalance())
                .build();
        movementRepository.save(destinationMovement);

        return mapToTransactionResponse(savedTransaction);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .originWalletId(transaction.getOriginWallet().getId())
                .destinationWalletId(transaction.getDestinationWallet().getId())
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