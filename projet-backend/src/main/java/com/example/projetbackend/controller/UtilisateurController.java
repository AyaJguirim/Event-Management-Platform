package com.example.projetbackend.controller;

import com.example.projetbackend.dto.UtilisateurRequestDTO;
import com.example.projetbackend.dto.UtilisateurResponseDTO;
import com.example.projetbackend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping
    public List<UtilisateurResponseDTO> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> getUtilisateurById(@PathVariable Long id) {
        UtilisateurResponseDTO utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponseDTO> addUtilisateur(@RequestBody UtilisateurRequestDTO dto) {
        UtilisateurResponseDTO nouveau = utilisateurService.addUtilisateur(dto);
        if (nouveau == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurRequestDTO dto) {
        UtilisateurResponseDTO modifie = utilisateurService.updateUtilisateur(id, dto);
        if (modifie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(modifie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}