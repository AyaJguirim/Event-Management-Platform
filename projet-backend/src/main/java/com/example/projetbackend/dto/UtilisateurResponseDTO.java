package com.example.projetbackend.dto;

import java.time.LocalDateTime;

public class UtilisateurResponseDTO {

    private final Long id;
    private final String nom;
    private final String prenom;
    private final String email;
    private final String role;
    private final boolean actif;
    private final LocalDateTime dateCreation;
    private final boolean emailVerifie;

    public UtilisateurResponseDTO(Long id, String nom, String prenom, String email, String role, boolean actif, LocalDateTime dateCreation, boolean emailVerifie) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.emailVerifie = emailVerifie;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isActif() { return actif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public boolean isEmailVerified() { return emailVerifie; }
}