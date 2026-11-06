package com.example.projetbackend.controller;

import com.example.projetbackend.dto.*;
import com.example.projetbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UtilisateurResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        UtilisateurResponseDTO nouveau = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO reponse = authService.login(dto);
        return ResponseEntity.ok(reponse);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(@Valid @RequestBody ResendVerificationRequestDTO dto) {
        authService.resendVerification(dto);
        return ResponseEntity.ok().build();
    }
}