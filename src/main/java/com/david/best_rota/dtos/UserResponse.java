package com.david.best_rota.dtos;

import com.david.best_rota.Enums.StatusConta;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        StatusConta statusConta,
        LocalDateTime assinaturaExpiraEm
) {
}
