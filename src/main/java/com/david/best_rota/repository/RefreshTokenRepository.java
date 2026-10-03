package com.david.best_rota.repository;

import com.david.best_rota.entity.RefreshToken;
import com.david.best_rota.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUsuario(Usuario usuario);
}
