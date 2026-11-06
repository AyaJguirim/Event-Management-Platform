package com.example.projetbackend.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EvenementRequestDTO {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime date;

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @Min(value = 1, message = "La capacité doit être supérieure à 0")
    private int capacite;

    @PositiveOrZero(message = "Le prix ne peut pas être négatif")
    private double prix;

    private boolean actif;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categorieId;

    public EvenementRequestDTO() {
    }

    public String getTitre() { return titre; }

    public String getDescription() { return description; }

    public LocalDateTime getDate() { return date; }

    public String getLieu() { return lieu; }

    public int getCapacite() { return capacite; }

    public boolean isActif() { return actif; }

    public Long getCategorieId() { return categorieId; }

    public void setTitre(String titre) { this.titre = titre; }

    public void setDescription(String description) { this.description = description; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public void setLieu(String lieu) { this.lieu = lieu; }

    public void setCapacite(int capacite) { this.capacite = capacite; }

    public double getPrix() { return prix; }

    public void setPrix(double prix) { this.prix = prix; }

    public void setActif(boolean actif) { this.actif = actif; }

    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
}