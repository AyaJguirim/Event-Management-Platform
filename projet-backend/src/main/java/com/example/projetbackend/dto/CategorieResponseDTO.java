package com.example.projetbackend.dto;

public class CategorieResponseDTO {
    private Long id;
    private String nom;
    private String description;

    public CategorieResponseDTO() {
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }

    public String getDescription() { return description; }

    public void setId(Long id) { this.id = id; }

    public void setNom(String nom) { this.nom = nom; }

    public void setDescription(String description) { this.description = description; }
}