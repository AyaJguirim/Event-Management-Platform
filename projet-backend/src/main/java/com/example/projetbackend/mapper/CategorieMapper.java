package com.example.projetbackend.mapper;

import com.example.projetbackend.dto.CategorieRequestDTO;
import com.example.projetbackend.dto.CategorieResponseDTO;
import com.example.projetbackend.entity.Categorie;

public final class CategorieMapper {

    // Empêche la création d'une instance de cette classe utilitaire.
    private CategorieMapper() {
    }

    /**
     * Convertit un DTO de requête en entité Categorie.
     */
    public static Categorie toEntity(CategorieRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Categorie categorie = new Categorie();
        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());

        return categorie;
    }

    /**
     * Convertit une entité Categorie en DTO de réponse.
     */
    public static CategorieResponseDTO toResponseDTO(Categorie categorie) {
        if (categorie == null) {
            return null;
        }

        CategorieResponseDTO dto = new CategorieResponseDTO();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getNom());
        dto.setDescription(categorie.getDescription());

        return dto;
    }

    /**
     * Met à jour une entité existante à partir d'un DTO de requête.
     * L'identifiant de la catégorie reste inchangé.
     */
    public static void updateEntity(
            CategorieRequestDTO dto,
            Categorie categorie
    ) {
        if (dto == null || categorie == null) {
            return;
        }

        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());
    }
}