package com.example.projetbackend.security;

import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Identifiants incorrects"));

        return User.withUsername(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .authorities("ROLE_" + utilisateur.getRole().name())
                .disabled(!utilisateur.isEstActif())
                .build();
    }
}