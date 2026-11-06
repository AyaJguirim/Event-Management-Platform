package com.example.projetbackend.config;

import com.example.projetbackend.entity.Role;
import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Locale;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String adminEmail,
            @Value("${app.admin.password:}") String adminPassword
    ) {
        return args -> {
            if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
                return;
            }

            String email = adminEmail.trim().toLowerCase(Locale.ROOT);

            if (utilisateurRepository.existsByEmailIgnoreCase(email)) {
                return;
            }

            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setPrenom("Plateforme");
            admin.setEmail(email);
            admin.setMotDePasse(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setEstActif(true);
            admin.setDateCreation(LocalDateTime.now());
            admin.setEmailVerifie(true);
            admin.setDateVerificationEmail(LocalDateTime.now());

            utilisateurRepository.save(admin);
        };
    }
}