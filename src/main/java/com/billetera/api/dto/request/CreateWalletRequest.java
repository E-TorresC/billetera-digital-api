package com.billetera.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateWalletRequest {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda debe tener exactamente 3 caracteres (ej. PEN, USD)")
    private String currency;
}