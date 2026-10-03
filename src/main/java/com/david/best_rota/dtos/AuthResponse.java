package com.david.best_rota.dtos;

public record AuthResponse(
    String accessToken,
    String refreshToken
) {}