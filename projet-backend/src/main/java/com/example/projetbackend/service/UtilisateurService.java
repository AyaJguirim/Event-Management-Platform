package com.example.projetbackend.service;

import com.example.projetbackend.dto.UtilisateurRequestDTO;
import com.example.projetbackend.dto.UtilisateurResponseDTO;
import com.example.projetbackend.entity.Role;
import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.mapper.UtilisateurMapper;
import com.example.projetbackend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<UtilisateurResponseDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurResponseDTO getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurMapper::toResponseDTO)
                .orElse(null);
    }

    // Création - utilisée pour l'inscription publique (RG : email unique)
    public UtilisateurResponseDTO addUtilisateur(UtilisateurRequestDTO dto) {
        if (utilisateurRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            return null;
        }

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setRole(Role.ADMIN); // cette route ADMIN-only sert précisément à créer d'autres admins
        utilisateur.setEstActif(true);
        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setEmailVerifie(true); // créé directement par un admin, pas besoin de vérification email

        Utilisateur enregistre = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toResponseDTO(enregistre);
    }

    public UtilisateurResponseDTO updateUtilisateur(Long id, UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur == null) {
            return null;
        }

        UtilisateurMapper.updateEntity(dto, utilisateur);
        Utilisateur misAJour = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toResponseDTO(misAJour);
    }

    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}