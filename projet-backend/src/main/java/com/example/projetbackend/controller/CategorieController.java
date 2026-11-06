package com.example.projetbackend.controller;

import com.example.projetbackend.dto.CategorieRequestDTO;
import com.example.projetbackend.dto.CategorieResponseDTO;
import com.example.projetbackend.service.CategorieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    @GetMapping
    public List<CategorieResponseDTO> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponseDTO> getCategorieById(@PathVariable Long id) {
        CategorieResponseDTO categorie = categorieService.getCategorieById(id);
        if (categorie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categorie);
    }

    @PostMapping
    public ResponseEntity<CategorieResponseDTO> addCategorie(@Valid @RequestBody CategorieRequestDTO dto) {
        CategorieResponseDTO nouvelle = categorieService.addCategorie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategorieResponseDTO> updateCategorie(@Valid @PathVariable Long id, @RequestBody CategorieRequestDTO dto) {
        CategorieResponseDTO modifiee = categorieService.updateCategorie(id, dto);
        if (modifiee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(modifiee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        CategorieResponseDTO categorie = categorieService.getCategorieById(id);
        if (categorie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}