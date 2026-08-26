package com.billetera.api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String names;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String lastNames;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "estado", nullable = false)
    private Boolean status; // true: activo, false: inactivo

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = true;
        }
    }
}