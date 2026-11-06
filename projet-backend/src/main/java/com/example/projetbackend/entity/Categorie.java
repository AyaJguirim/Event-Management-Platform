package com.example.projetbackend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nom;

    @Column(nullable = true, length = 50)
    private String description;

    @OneToMany(mappedBy = "categorie")
    private List<Evenement> evenements;

    public Categorie() {
    }

    public Categorie(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }

    public String getDescription() { return description; }

    public List<Evenement> getEvenements() { return evenements; }

    public void setId(Long id) { this.id = id; }

    public void setNom(String nom) { this.nom = nom; }

    public void setDescription(String description) { this.description = description; }

    public void setEvenements(List<Evenement> evenements) { this.evenements = evenements; }
}