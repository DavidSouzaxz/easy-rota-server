package com.david.best_rota.controller;

import com.david.best_rota.Enums.Role;
import com.david.best_rota.Enums.StatusConta;
import com.david.best_rota.dtos.AuthResponse;
import com.david.best_rota.dtos.LoginRequest;
import com.david.best_rota.dtos.RefreshRequest;
import com.david.best_rota.dtos.RegisterRequest;
import com.david.best_rota.entity.RefreshToken;
import com.david.best_rota.entity.Usuario;
import com.david.best_rota.repository.UserRepository;
import com.david.best_rota.services.JwtService;
import com.david.best_rota.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Usuario user = new Usuario();
        user.setEmail(request.email());
        user.setSenha(passwordEncoder.encode(request.senha()));
        user.setRole(Role.USER);
        user.setStatusConta(StatusConta.ATIVO);
        userRepository.save(user);
        return ResponseEntity.ok("Registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest request) {
        return refreshTokenService.findByToken(request.token())
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUsuario)
            .map(user -> {
                String accessToken = jwtService.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(accessToken, request.token()));
            }).orElseThrow(() -> new RuntimeException("Refresh token inválido"));
    }
}