package com.david.best_rota.controller;

import com.david.best_rota.Enums.StatusConta;
import com.david.best_rota.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;

    @PutMapping("/users/{id}/subscription")
    public ResponseEntity<?> activateSubscription(@PathVariable Long id, @RequestParam int dias) {
        var user = userRepository.findById(id).orElseThrow();
        user.setAssinaturaExpiraEm(LocalDateTime.now().plusDays(dias));
        userRepository.save(user);
        return ResponseEntity.ok("Assinatura atualizada");
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElseThrow();
        user.setStatusConta(StatusConta.BANIDO);
        userRepository.save(user);
        return ResponseEntity.ok("Usuário banido");
    }
}