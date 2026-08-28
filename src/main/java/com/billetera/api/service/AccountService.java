package com.billetera.api.service;

import com.billetera.api.dto.request.CreateAccountRequest;
import com.billetera.api.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    AccountResponse getAccountById(Long id);
    List<AccountResponse> getAccountsByUserId(Long userId);
}