package com.example.projetbackend.repository;

import com.example.projetbackend.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByTitreContainingIgnoreCase(String titre);

    List<Evenement> findByCategorieId(Long categorieId);

    List<Evenement> findByActifTrue();

    List<Evenement> findByPrixLessThan(double prix);

    boolean existsByCategorieId(Long categorieId);

    List<Evenement> findByDateBetween(LocalDateTime debut, LocalDateTime fin);
}