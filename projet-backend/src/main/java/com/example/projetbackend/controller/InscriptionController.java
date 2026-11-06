package com.example.projetbackend.controller;

import com.example.projetbackend.dto.ChangerEtatRequestDTO;
import com.example.projetbackend.dto.InscriptionRequestDTO;
import com.example.projetbackend.dto.InscriptionResponseDTO;
import com.example.projetbackend.service.InscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    // GET /api/inscriptions - organisateur, toutes les inscriptions
    @GetMapping
    public List<InscriptionResponseDTO> getAllInscriptions() {
        return inscriptionService.getAllInscriptions();
    }

    // GET /api/inscriptions/mes-inscriptions - participant connecté, ses propres inscriptions
    @GetMapping("/mes-inscriptions")
    public List<InscriptionResponseDTO> getMesInscriptions(@AuthenticationPrincipal Jwt jwt) {
        return inscriptionService.getMesInscriptions(jwt.getSubject());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscriptionResponseDTO> getInscriptionById(@PathVariable Long id) {
        InscriptionResponseDTO inscription = inscriptionService.getInscriptionById(id);
        if (inscription == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(inscription);
    }

    // POST /api/inscriptions - le participant connecté s'inscrit (utilisateur déduit du token)
    @PostMapping
    public ResponseEntity<InscriptionResponseDTO> creerInscription(@Valid @AuthenticationPrincipal Jwt jwt, @RequestBody InscriptionRequestDTO dto) {
        InscriptionResponseDTO inscription = inscriptionService.creerInscription(jwt.getSubject(), dto);
        if (inscription == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

    // PUT /api/inscriptions/{id}/annuler - le participant connecté annule sa propre inscription
    @PutMapping("/{id}/annuler")
    public ResponseEntity<InscriptionResponseDTO> annulerInscription(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        InscriptionResponseDTO inscription = inscriptionService.annulerInscription(jwt.getSubject(), id);
        if (inscription == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(inscription);
    }

    // PUT /api/inscriptions/{id}/etat - organisateur change l'état
    @PutMapping("/{id}/etat")
    public ResponseEntity<InscriptionResponseDTO> changerEtat(@PathVariable Long id, @RequestBody ChangerEtatRequestDTO dto) {
        InscriptionResponseDTO inscription = inscriptionService.changerEtat(id, dto.getEtat());
        if (inscription == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(inscription);
    }
}