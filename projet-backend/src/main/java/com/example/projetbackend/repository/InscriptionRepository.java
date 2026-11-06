package com.example.projetbackend.repository;

import com.example.projetbackend.entity.EtatInscription;
import com.example.projetbackend.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByUtilisateurId(Long utilisateurId);

    List<Inscription> findByEvenementId(Long evenementId);

    boolean existsByUtilisateurIdAndEvenementIdAndEtatIn(Long utilisateurId, Long evenementId, List<EtatInscription> etats);

    long countByEvenementIdAndEtatIn(Long evenementId, List<EtatInscription> etats);

    boolean existsByEvenementId(Long evenementId);
}