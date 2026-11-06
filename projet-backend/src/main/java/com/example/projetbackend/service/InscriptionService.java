package com.example.projetbackend.service;

import com.example.projetbackend.dto.InscriptionRequestDTO;
import com.example.projetbackend.dto.InscriptionResponseDTO;
import com.example.projetbackend.entity.EtatInscription;
import com.example.projetbackend.entity.Evenement;
import com.example.projetbackend.entity.Inscription;
import com.example.projetbackend.entity.Utilisateur;
import com.example.projetbackend.exception.AccesRefuseException;
import com.example.projetbackend.exception.ResourceNotFoundException;
import com.example.projetbackend.mapper.InscriptionMapper;
import com.example.projetbackend.repository.EvenementRepository;
import com.example.projetbackend.repository.InscriptionRepository;
import com.example.projetbackend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionService {

    private static final List<EtatInscription> ETATS_ACTIFS = Arrays.asList(
            EtatInscription.EN_ATTENTE, EtatInscription.CONFIRMEE
    );

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EvenementRepository evenementRepository;

    // Consultation - organisateur (toutes les inscriptions)
    public List<InscriptionResponseDTO> getAllInscriptions() {
        return inscriptionRepository.findAll()
                .stream()
                .map(InscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Consultation - participant (ses propres inscriptions, déduites du token)
    public List<InscriptionResponseDTO> getMesInscriptions(String emailUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(emailUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        return inscriptionRepository.findByUtilisateurId(utilisateur.getId())
                .stream()
                .map(InscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public InscriptionResponseDTO getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .map(InscriptionMapper::toResponseDTO)
                .orElse(null);
    }

    // Création - applique RG1 et RG2, utilisateur déduit du token (pas de l'URL/du corps)
    @Transactional
    public InscriptionResponseDTO creerInscription(String emailUtilisateur, InscriptionRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(emailUtilisateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Evenement evenement = evenementRepository.findById(dto.getEvenementId()).orElse(null);
        if (evenement == null) {
            return null;
        }

        boolean dejaInscrit = inscriptionRepository.existsByUtilisateurIdAndEvenementIdAndEtatIn(
                utilisateur.getId(), dto.getEvenementId(), ETATS_ACTIFS
        );
        if (dejaInscrit) {
            return null;
        }

        long inscriptionsActives = inscriptionRepository.countByEvenementIdAndEtatIn(
                dto.getEvenementId(), ETATS_ACTIFS
        );
        if (inscriptionsActives >= evenement.getCapacite()) {
            return null;
        }

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setEvenement(evenement);
        inscription.setDateInscription(LocalDateTime.now());
        inscription.setEtat(EtatInscription.EN_ATTENTE);

        Inscription enregistree = inscriptionRepository.save(inscription);
        return InscriptionMapper.toResponseDTO(enregistree);
    }

    // Annulation - vérifie que l'appelant est bien le propriétaire de l'inscription
    public InscriptionResponseDTO annulerInscription(String emailUtilisateur, Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription introuvable"));

        if (!inscription.getUtilisateur().getEmail().equalsIgnoreCase(emailUtilisateur)) {
            throw new AccesRefuseException("Vous ne pouvez annuler que vos propres inscriptions");
        }

        if (!ETATS_ACTIFS.contains(inscription.getEtat())) {
            return null;
        }

        inscription.setEtat(EtatInscription.ANNULEE);
        Inscription misAJour = inscriptionRepository.save(inscription);
        return InscriptionMapper.toResponseDTO(misAJour);
    }

    // Changement d'état - organisateur uniquement (RG7)
    public InscriptionResponseDTO changerEtat(Long id, EtatInscription nouvelEtat) {
        Inscription inscription = inscriptionRepository.findById(id).orElse(null);
        if (inscription == null) {
            return null;
        }

        if (!transitionValide(inscription.getEtat(), nouvelEtat)) {
            return null;
        }

        inscription.setEtat(nouvelEtat);
        Inscription misAJour = inscriptionRepository.save(inscription);
        return InscriptionMapper.toResponseDTO(misAJour);
    }

    private boolean transitionValide(EtatInscription actuel, EtatInscription nouveau) {
        if (actuel == nouveau) {
            return false;
        }
        return switch (actuel) {
            case EN_ATTENTE -> nouveau == EtatInscription.CONFIRMEE || nouveau == EtatInscription.ANNULEE;
            case CONFIRMEE -> nouveau == EtatInscription.TERMINEE || nouveau == EtatInscription.ANNULEE;
            case ANNULEE, TERMINEE -> false;
        };
    }
}