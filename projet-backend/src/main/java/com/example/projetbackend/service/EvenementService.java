package com.example.projetbackend.service;

import com.example.projetbackend.dto.EvenementRequestDTO;
import com.example.projetbackend.dto.EvenementResponseDTO;
import com.example.projetbackend.entity.Categorie;
import com.example.projetbackend.entity.EtatInscription;
import com.example.projetbackend.entity.Evenement;
import com.example.projetbackend.exception.RessourceUtiliseeException;
import com.example.projetbackend.mapper.EvenementMapper;
import com.example.projetbackend.repository.CategorieRepository;
import com.example.projetbackend.repository.EvenementRepository;
import com.example.projetbackend.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvenementService {

    private static final List<EtatInscription> ETATS_ACTIFS = Arrays.asList(
            EtatInscription.EN_ATTENTE, EtatInscription.CONFIRMEE
    );

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    public List<EvenementResponseDTO> getAllEvenements() {
        return evenementRepository.findAll()
                .stream()
                .map(this::toResponseDTOAvecPlaces)
                .collect(Collectors.toList());
    }

    public EvenementResponseDTO getEvenementById(Long id) {
        return evenementRepository.findById(id)
                .map(this::toResponseDTOAvecPlaces)
                .orElse(null);
    }

    // Recherche/filtrage - exigés par le cahier des charges (section 6)
    public List<EvenementResponseDTO> rechercherParTitre(String titre) {
        return evenementRepository.findByTitreContainingIgnoreCase(titre)
                .stream()
                .map(this::toResponseDTOAvecPlaces)
                .collect(Collectors.toList());
    }

    public List<EvenementResponseDTO> filtrerParCategorie(Long categorieId) {
        return evenementRepository.findByCategorieId(categorieId)
                .stream()
                .map(this::toResponseDTOAvecPlaces)
                .collect(Collectors.toList());
    }

    public EvenementResponseDTO addEvenement(EvenementRequestDTO dto) {
        Categorie categorie = categorieRepository.findById(dto.getCategorieId()).orElse(null);
        if (categorie == null) {
            return null;
        }

        Evenement evenement = EvenementMapper.toEntity(dto);
        evenement.setCategorie(categorie);

        Evenement enregistre = evenementRepository.save(evenement);
        return toResponseDTOAvecPlaces(enregistre);
    }

    public EvenementResponseDTO updateEvenement(Long id, EvenementRequestDTO dto) {
        Evenement evenement = evenementRepository.findById(id).orElse(null);
        if (evenement == null) {
            return null;
        }

        Categorie categorie = categorieRepository.findById(dto.getCategorieId()).orElse(null);
        if (categorie == null) {
            return null;
        }

        EvenementMapper.updateEntity(dto, evenement);
        evenement.setCategorie(categorie);

        Evenement misAJour = evenementRepository.save(evenement);
        return toResponseDTOAvecPlaces(misAJour);
    }

    public boolean deleteEvenement(Long id) {
        boolean aDesInscriptions = inscriptionRepository.existsByEvenementId(id);
        if (aDesInscriptions) {
            return false;
        }

        evenementRepository.deleteById(id);
        return true;
    }

    public List<EvenementResponseDTO> filtrerParDate(LocalDateTime debut, LocalDateTime fin) {
        return evenementRepository.findByDateBetween(debut, fin)
                .stream()
                .map(this::toResponseDTOAvecPlaces)
                .collect(Collectors.toList());
    }

    // Enrichit le DTO avec le nombre de places disponibles (capacite - inscriptions actives)
    private EvenementResponseDTO toResponseDTOAvecPlaces(Evenement evenement) {
        EvenementResponseDTO dto = EvenementMapper.toResponseDTO(evenement);

        long inscriptionsActives = inscriptionRepository.countByEvenementIdAndEtatIn(
                evenement.getId(), ETATS_ACTIFS
        );

        long placesDisponibles = evenement.getCapacite() - inscriptionsActives;
        dto.setPlacesDisponibles(Math.max(placesDisponibles, 0));

        return dto;
    }
}