package com.example.projetbackend.service;

import com.example.projetbackend.dto.*;
import com.example.projetbackend.entity.Role;
import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.exception.EmailDejaUtiliseException;
import com.example.projetbackend.exception.ResourceNotFoundException;
import com.example.projetbackend.exception.VerificationTokenInvalidException;
import com.example.projetbackend.mapper.UtilisateurMapper;
import com.example.projetbackend.repository.UtilisateurRepository;
import com.example.projetbackend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Transactional
    public UtilisateurResponseDTO register(RegisterRequestDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();

        if (utilisateurRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailDejaUtiliseException("Cette adresse email est déjà utilisée");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setRole(Role.USER);
        utilisateur.setEstActif(true);
        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setEmailVerifie(false);

        Utilisateur enregistre = utilisateurRepository.save(utilisateur);

        emailVerificationService.creerEtEnvoyerToken(enregistre);

        return UtilisateurMapper.toResponseDTO(enregistre);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (!utilisateur.isEmailVerifie()) {
            throw new VerificationTokenInvalidException("Veuillez vérifier votre adresse email avant de vous connecter");
        }

        String token = jwtService.generateToken(authentication);

        return new AuthResponseDTO(token, "Bearer", jwtService.getExpirationSeconds(), utilisateur.getEmail(), utilisateur.getRole().name(), utilisateur.getNom(), utilisateur.getPrenom());
    }

    public void verifyEmail(String token) {
        emailVerificationService.verifierEmail(token);
    }

    public void resendVerification(ResendVerificationRequestDTO dto) {
        emailVerificationService.renvoyerEmailVerification(dto.getEmail());
    }
}