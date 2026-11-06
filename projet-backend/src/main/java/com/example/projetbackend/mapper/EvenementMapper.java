package com.example.projetbackend.mapper;

import com.example.projetbackend.dto.EvenementRequestDTO;
import com.example.projetbackend.dto.EvenementResponseDTO;
import com.example.projetbackend.entity.Evenement;

public final class EvenementMapper {

    private EvenementMapper() {
    }

    public static Evenement toEntity(EvenementRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Evenement evenement = new Evenement();
        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setDate(dto.getDate());
        evenement.setLieu(dto.getLieu());
        evenement.setCapacite(dto.getCapacite());
        evenement.setActif(dto.isActif());
        evenement.setPrix(dto.getPrix());
        // categorie assignée dans le service, après recherche via categorieId

        return evenement;
    }

    public static EvenementResponseDTO toResponseDTO(Evenement evenement) {
        if (evenement == null) {
            return null;
        }

        EvenementResponseDTO dto = new EvenementResponseDTO();
        dto.setId(evenement.getId());
        dto.setTitre(evenement.getTitre());
        dto.setDescription(evenement.getDescription());
        dto.setDate(evenement.getDate());
        dto.setLieu(evenement.getLieu());
        dto.setCapacite(evenement.getCapacite());
        dto.setActif(evenement.isActif());
        dto.setPrix(evenement.getPrix());

        if (evenement.getCategorie() != null) {
            dto.setCategorieId(evenement.getCategorie().getId());
            dto.setCategorieNom(evenement.getCategorie().getNom());
        }

        return dto;
    }

    public static void updateEntity(EvenementRequestDTO dto, Evenement evenement) {
        if (dto == null || evenement == null) {
            return;
        }

        evenement.setTitre(dto.getTitre());
        evenement.setDescription(dto.getDescription());
        evenement.setDate(dto.getDate());
        evenement.setLieu(dto.getLieu());
        evenement.setCapacite(dto.getCapacite());
        evenement.setActif(dto.isActif());
        evenement.setPrix(dto.getPrix());
        // categorie mise à jour dans le service si categorieId a changé
    }
}