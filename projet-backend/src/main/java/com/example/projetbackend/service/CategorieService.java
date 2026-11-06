package com.example.projetbackend.service;

import com.example.projetbackend.dto.CategorieRequestDTO;
import com.example.projetbackend.dto.CategorieResponseDTO;
import com.example.projetbackend.entity.Categorie;
import com.example.projetbackend.exception.RessourceUtiliseeException;
import com.example.projetbackend.mapper.CategorieMapper;
import com.example.projetbackend.repository.CategorieRepository;
import com.example.projetbackend.repository.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private EvenementRepository evenementRepository;

    public List<CategorieResponseDTO> getAllCategories() {
        return categorieRepository.findAll()
                .stream()
                .map(CategorieMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategorieResponseDTO getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .map(CategorieMapper::toResponseDTO)
                .orElse(null);
    }

    public CategorieResponseDTO addCategorie(CategorieRequestDTO dto) {
        Categorie categorie = CategorieMapper.toEntity(dto);
        Categorie enregistree = categorieRepository.save(categorie);
        return CategorieMapper.toResponseDTO(enregistree);
    }

    public CategorieResponseDTO updateCategorie(Long id, CategorieRequestDTO dto) {
        Categorie categorie = categorieRepository.findById(id).orElse(null);
        if (categorie == null) {
            return null;
        }
        CategorieMapper.updateEntity(dto, categorie);
        Categorie misAJour = categorieRepository.save(categorie);
        return CategorieMapper.toResponseDTO(misAJour);
    }

    public void deleteCategorie(Long id) {
        boolean estUtilisee = evenementRepository.existsByCategorieId(id);
        if (estUtilisee) {
            throw new RessourceUtiliseeException("Cette catégorie est encore utilisée par au moins un événement et ne peut pas être supprimée");
        }
        categorieRepository.deleteById(id);
    }
}