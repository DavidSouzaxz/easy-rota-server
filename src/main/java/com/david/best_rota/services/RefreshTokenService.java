package com.david.best_rota.services;

import com.david.best_rota.entity.RefreshToken;
import com.david.best_rota.entity.Usuario;
import com.david.best_rota.repository.RefreshTokenRepository;
import com.david.best_rota.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refresh-token.expiration}") private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired private UserRepository userRepository;

    public RefreshToken createRefreshToken(Long userId) {
        Usuario usuario = userRepository.findById(userId).get();

        RefreshToken refreshToken = refreshTokenRepository.findByUsuario(usuario)
                .orElse(new RefreshToken());
        refreshToken.setUsuario(userRepository.findById(userId).get());
        refreshToken.setDataExpiracao(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getDataExpiracao().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirado. Faça login novamente.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}