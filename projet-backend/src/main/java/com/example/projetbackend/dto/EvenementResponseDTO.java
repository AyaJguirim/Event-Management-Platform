package com.example.projetbackend.dto;

import java.time.LocalDateTime;

public class EvenementResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime date;
    private String lieu;
    private int capacite;
    private double prix;
    private boolean actif;
    private Long categorieId;
    private String categorieNom;
    private long placesDisponibles;

    public EvenementResponseDTO() {
    }

    public Long getId() { return id; }

    public String getTitre() { return titre; }

    public String getDescription() { return description; }

    public LocalDateTime getDate() { return date; }

    public String getLieu() { return lieu; }

    public int getCapacite() { return capacite; }

    public boolean isActif() { return actif; }

    public Long getCategorieId() { return categorieId; }

    public String getCategorieNom() { return categorieNom; }

    public void setId(Long id) { this.id = id; }

    public void setTitre(String titre) { this.titre = titre; }

    public void setDescription(String description) { this.description = description; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public void setLieu(String lieu) { this.lieu = lieu; }

    public void setCapacite(int capacite) { this.capacite = capacite; }

    public double getPrix() { return prix; }

    public void setPrix(double prix) { this.prix = prix; }

    public void setActif(boolean actif) { this.actif = actif; }

    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }

    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }

    public long getPlacesDisponibles() { return placesDisponibles; }

    public void setPlacesDisponibles(long placesDisponibles) { this.placesDisponibles = placesDisponibles; }
}