package com.david.best_rota.controller;

import com.david.best_rota.dtos.UserResponse;
import com.david.best_rota.entity.Usuario;
import com.david.best_rota.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")

public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        UserResponse response = new UserResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getStatusConta(),
                usuario.getAssinaturaExpiraEm()
        );

        return ResponseEntity.ok(response);
    }


}
