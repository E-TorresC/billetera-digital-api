package com.billetera.api.domain.model;

import com.billetera.api.domain.enums.TransactionStatus;
import com.billetera.api.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones", indexes = {
        @Index(name = "idx_tx_idempotency", columnList = "clave_idempotencia"),
        @Index(name = "idx_tx_fecha", columnList = "fecha_creacion")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_wallet_origen")
    private Wallet originWallet; // Nullable (ej. Depósitos)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_wallet_destino")
    private Wallet destinationWallet; // Nullable (ej. Retiros)

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TransactionType type;

    @Column(name = "monto", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "referencia", length = 100)
    private String reference;

    @Column(name = "clave_idempotencia", length = 100)
    private String idempotencyKey;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = TransactionStatus.PENDING;
        }
    }
}