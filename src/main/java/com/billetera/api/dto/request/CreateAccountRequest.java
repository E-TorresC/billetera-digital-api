package com.billetera.api.dto.request;

import com.billetera.api.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 30, message = "El número de cuenta no debe superar 30 caracteres")
    private String accountNumber;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private AccountType type;
}