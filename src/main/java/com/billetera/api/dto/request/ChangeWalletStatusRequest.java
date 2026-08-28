package com.billetera.api.dto.request;

import com.billetera.api.domain.enums.WalletStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeWalletStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private WalletStatus status;
}