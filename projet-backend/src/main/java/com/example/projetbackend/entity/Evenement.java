package com.example.projetbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titre;

    @Column(nullable = true, length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 100)
    private String lieu;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private boolean actif;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "evenement")
    private List<Inscription> inscriptions;

    public Evenement() {
    }

    public Evenement(Long id, String titre, String description, LocalDateTime date, String lieu, int capacite, boolean actif, double prix, Categorie categorie) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.capacite = capacite;
        this.actif = actif;
        this.prix = prix;
        this.categorie = categorie;
    }

    public Long getId() { return id; }

    public String getTitre() { return titre; }

    public String getDescription() { return description; }

    public LocalDateTime getDate() { return date; }

    public String getLieu() { return lieu; }

    public int getCapacite() { return capacite; }

    public boolean isActif() { return actif; }

    public Categorie getCategorie() { return categorie; }

    public List<Inscription> getInscriptions() { return inscriptions; }

    public void setId(Long id) { this.id = id; }

    public void setTitre(String titre) { this.titre = titre; }

    public void setDescription(String description) { this.description = description; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public void setLieu(String lieu) { this.lieu = lieu; }

    public void setCapacite(int capacite) { this.capacite = capacite; }

    public double getPrix() { return prix; }

    public void setPrix(double prix) { this.prix = prix; }

    public void setActif(boolean actif) { this.actif = actif; }

    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public void setInscriptions(List<Inscription> inscriptions) { this.inscriptions = inscriptions; }
}