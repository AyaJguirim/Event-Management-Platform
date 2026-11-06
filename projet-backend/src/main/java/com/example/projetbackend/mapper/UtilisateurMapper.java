package com.example.projetbackend.mapper;

import com.example.projetbackend.dto.UtilisateurRequestDTO;
import com.example.projetbackend.dto.UtilisateurResponseDTO;
import com.example.projetbackend.entity.Utilisateur;

public final class UtilisateurMapper {

    private UtilisateurMapper() {
    }

    public static Utilisateur toEntity(UtilisateurRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(dto.getMotDePasse());

        return utilisateur;
    }

    public static UtilisateurResponseDTO toResponseDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        return new UtilisateurResponseDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getRole().name(),
                utilisateur.isEstActif(),
                utilisateur.getDateCreation(),
                utilisateur.isEmailVerifie()
        );
    }

    public static void updateEntity(UtilisateurRequestDTO dto, Utilisateur utilisateur) {
        if (dto == null || utilisateur == null) {
            return;
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
    }
}