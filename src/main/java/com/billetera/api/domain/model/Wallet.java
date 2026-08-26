package com.billetera.api.domain.model;

import com.billetera.api.domain.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wallet")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency; // Ej. "PEN", "USD"

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private WalletStatus status;

    @Version
    @Column(name = "version", nullable = false)
    private Long version; // Optimistic Locking

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        if (this.status == null) {
            this.status = WalletStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Métodos de Lógica de Dominio ---

    public boolean isActive() {
        return WalletStatus.ACTIVE.equals(this.status);
    }

    public void debit(BigDecimal amount) {
        if (!isActive()) {
            throw new IllegalStateException("La billetera no se encuentra activa");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar la operación");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (!isActive()) {
            throw new IllegalStateException("La billetera no se encuentra activa");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        this.balance = this.balance.add(amount);
    }
}