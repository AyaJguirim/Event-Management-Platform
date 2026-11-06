package com.example.projetbackend.controller;

import com.example.projetbackend.dto.EvenementRequestDTO;
import com.example.projetbackend.dto.EvenementResponseDTO;
import com.example.projetbackend.service.EvenementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/evenements")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @GetMapping
    public List<EvenementResponseDTO> getAllEvenements() {
        return evenementService.getAllEvenements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvenementResponseDTO> getEvenementById(@PathVariable Long id) {
        EvenementResponseDTO evenement = evenementService.getEvenementById(id);
        if (evenement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(evenement);
    }

    @PostMapping
    public ResponseEntity<EvenementResponseDTO> addEvenement(@Valid @RequestBody EvenementRequestDTO dto) {
        EvenementResponseDTO nouveau = evenementService.addEvenement(dto);
        if (nouveau == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvenementResponseDTO> updateEvenement(@Valid @PathVariable Long id, @RequestBody EvenementRequestDTO dto) {
        EvenementResponseDTO modifie = evenementService.updateEvenement(id, dto);
        if (modifie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(modifie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        boolean supprime = evenementService.deleteEvenement(id);
        if (!supprime) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.noContent().build();
    }
    // GET /api/evenements/recherche?titre=spring
    @GetMapping("/recherche")
    public List<EvenementResponseDTO> rechercherParTitre(@RequestParam String titre) {
        return evenementService.rechercherParTitre(titre);
    }

    // GET /api/evenements/categorie/{categorieId}
    @GetMapping("/categorie/{categorieId}")
    public List<EvenementResponseDTO> filtrerParCategorie(@PathVariable Long categorieId) {
        return evenementService.filtrerParCategorie(categorieId);
    }

    @GetMapping("/date")
    public List<EvenementResponseDTO> filtrerParDate(
            @RequestParam LocalDateTime debut,
            @RequestParam LocalDateTime fin
    ) {
        return evenementService.filtrerParDate(debut, fin);
    }
}