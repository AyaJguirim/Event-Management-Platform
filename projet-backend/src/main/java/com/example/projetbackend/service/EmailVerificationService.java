package com.example.projetbackend.service;

import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.entity.VerificationToken;
import com.example.projetbackend.exception.VerificationTokenExpiredException;
import com.example.projetbackend.exception.VerificationTokenInvalidException;
import com.example.projetbackend.repository.UtilisateurRepository;
import com.example.projetbackend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmailService emailService;

    private final String verificationBaseUrl;
    private final long expirationMinutes;

    public EmailVerificationService(
            VerificationTokenRepository tokenRepository,
            UtilisateurRepository utilisateurRepository,
            EmailService emailService,
            @Value("${app.verification.base-url}") String verificationBaseUrl,
            @Value("${app.verification.expiration-minutes}") long expirationMinutes
    ) {
        this.tokenRepository = tokenRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.emailService = emailService;
        this.verificationBaseUrl = verificationBaseUrl;
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional
    public void creerEtEnvoyerToken(Utilisateur utilisateur) {
        LocalDateTime maintenant = LocalDateTime.now();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setDateCreation(maintenant);
        verificationToken.setDateExpiration(maintenant.plusMinutes(expirationMinutes));
        verificationToken.setUtilisateur(utilisateur);

        tokenRepository.save(verificationToken);

        String lienVerification = verificationBaseUrl + "?token=" + verificationToken.getToken();

        emailService.envoyerEmailVerification(utilisateur.getEmail(), utilisateur.getPrenom(), lienVerification);
    }

    @Transactional
    public void verifierEmail(String valeurToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(valeurToken)
                .orElseThrow(() -> new VerificationTokenInvalidException("Le lien de vérification est invalide"));

        if (verificationToken.estUtilise()) {
            throw new VerificationTokenInvalidException("Ce lien de vérification a déjà été utilisé");
        }
        if (verificationToken.estExpire()) {
            throw new VerificationTokenExpiredException("Le lien de vérification a expiré");
        }

        Utilisateur utilisateur = verificationToken.getUtilisateur();
        if (utilisateur.isEmailVerifie()) {
            throw new VerificationTokenInvalidException("Cette adresse email est déjà vérifiée");
        }

        utilisateur.setEmailVerifie(true);
        utilisateur.setDateVerificationEmail(LocalDateTime.now());
        verificationToken.setDateUtilisation(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);
        tokenRepository.save(verificationToken);
    }

    @Transactional
    public void renvoyerEmailVerification(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new VerificationTokenInvalidException("Aucun compte ne correspond à cette adresse email"));

        if (utilisateur.isEmailVerifie()) {
            throw new VerificationTokenInvalidException("Cette adresse email est déjà vérifiée");
        }

        creerEtEnvoyerToken(utilisateur);
    }
}