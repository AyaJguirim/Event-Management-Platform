package com.example.projetbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean estActif;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private boolean emailVerifie = false;

    @Column(name = "date_verification_email")
    private LocalDateTime dateVerificationEmail;

    @OneToMany(mappedBy = "utilisateur")
    private List<Inscription> inscriptions;

    public Utilisateur() {
    }

    public Utilisateur(Long id, String nom, String prenom, String email, String motDePasse, Role role, boolean estActif, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.estActif = estActif;
        this.dateCreation = dateCreation;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }

    public String getPrenom() { return prenom; }

    public String getEmail() { return email; }

    public String getMotDePasse() { return motDePasse; }

    public Role getRole() { return role; }

    public boolean isEstActif() { return estActif; }

    public LocalDateTime getDateCreation() { return dateCreation; }

    public boolean isEmailVerifie() { return emailVerifie; }

    public LocalDateTime getDateVerificationEmail() { return dateVerificationEmail; }

    public List<Inscription> getInscriptions() { return inscriptions; }

    public void setId(Long id) { this.id = id; }

    public void setNom(String nom) { this.nom = nom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public void setEmail(String email) { this.email = email; }

    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public void setRole(Role role) { this.role = role; }

    public void setEstActif(boolean estActif) { this.estActif = estActif; }

    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public void setEmailVerifie(boolean emailVerifie) { this.emailVerifie = emailVerifie; }

    public void setDateVerificationEmail(LocalDateTime dateVerificationEmail) { this.dateVerificationEmail = dateVerificationEmail; }

    public void setInscriptions(List<Inscription> inscriptions) { this.inscriptions = inscriptions; }
}