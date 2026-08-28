package com.billetera.api.repository;

import com.billetera.api.domain.model.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByUserId(Long userId);

    // Consulta con Bloqueo Pesimista de Escritura para un solo registro
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdWithPessimisticLock(@Param("id") Long id);

    // Consulta con Bloqueo Pesimista ordenado por ID para prevenir Deadlocks en Transferencias
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id IN :ids ORDER BY w.id ASC")
    List<Wallet> findAllByIdsWithPessimisticLockOrdered(@Param("ids") List<Long> ids);
}