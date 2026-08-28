package com.billetera.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "El ID de la billetera de origen es obligatorio")
    private Long originWalletId;

    @NotNull(message = "El ID de la billetera de destino es obligatorio")
    private Long destinationWalletId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto a transferir debe ser mayor que cero")
    private BigDecimal amount;

    @Size(max = 100, message = "La referencia no debe superar los 100 caracteres")
    private String reference;
}