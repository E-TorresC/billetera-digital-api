package com.billetera.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String names;
    private String lastNames;
    private String email;
    private Boolean status;
    private LocalDateTime createdAt;
}