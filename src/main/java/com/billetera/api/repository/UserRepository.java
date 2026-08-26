// UserRepository.java
package com.billetera.api.repository;

import com.billetera.api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndStatusTrue(Long id);
    List<User> findByStatusTrue();
    boolean existsByEmail(String email);
}