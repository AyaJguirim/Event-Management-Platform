package com.example.projetbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EtatInscription etat;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;

    public Inscription() {
    }

    public Inscription(Long id, LocalDateTime dateInscription, EtatInscription etat, Utilisateur utilisateur, Evenement evenement) {
        this.id = id;
        this.dateInscription = dateInscription;
        this.etat = etat;
        this.utilisateur = utilisateur;
        this.evenement = evenement;
    }

    public Long getId() { return id; }

    public LocalDateTime getDateInscription() { return dateInscription; }

    public EtatInscription getEtat() { return etat; }

    public Utilisateur getUtilisateur() { return utilisateur; }

    public Evenement getEvenement() { return evenement; }

    public void setId(Long id) { this.id = id; }

    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public void setEtat(EtatInscription etat) { this.etat = etat; }

    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public void setEvenement(Evenement evenement) { this.evenement = evenement; }
}