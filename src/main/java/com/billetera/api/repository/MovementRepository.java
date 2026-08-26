// MovementRepository.java
package com.billetera.api.repository;

import com.billetera.api.domain.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}