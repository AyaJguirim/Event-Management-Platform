package com.example.projetbackend.mapper;

import com.example.projetbackend.dto.InscriptionResponseDTO;
import com.example.projetbackend.entity.Inscription;

public final class InscriptionMapper {

    private InscriptionMapper() {
    }

    public static InscriptionResponseDTO toResponseDTO(Inscription inscription) {
        if (inscription == null) {
            return null;
        }

        InscriptionResponseDTO dto = new InscriptionResponseDTO();
        dto.setId(inscription.getId());
        dto.setDateInscription(inscription.getDateInscription());
        dto.setEtat(inscription.getEtat());

        if (inscription.getUtilisateur() != null) {
            dto.setUtilisateurId(inscription.getUtilisateur().getId());
            dto.setUtilisateurNomComplet(
                    inscription.getUtilisateur().getPrenom() + " " + inscription.getUtilisateur().getNom()
            );
        }

        if (inscription.getEvenement() != null) {
            dto.setEvenementId(inscription.getEvenement().getId());
            dto.setEvenementTitre(inscription.getEvenement().getTitre());
        }

        return dto;
    }
}