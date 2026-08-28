package com.billetera.api.service.impl;

import com.billetera.api.domain.enums.AccountStatus;
import com.billetera.api.domain.model.Account;
import com.billetera.api.domain.model.User;
import com.billetera.api.dto.request.CreateAccountRequest;
import com.billetera.api.dto.response.AccountResponse;
import com.billetera.api.exception.ResourceNotFoundException;
import com.billetera.api.repository.AccountRepository;
import com.billetera.api.repository.UserRepository;
import com.billetera.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findByIdAndStatusTrue(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado o inactivo con ID: " + request.getUserId()));

        Account account = Account.builder()
                .user(user)
                .accountNumber(request.getAccountNumber())
                .type(request.getType())
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        return mapToResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
        return mapToResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}