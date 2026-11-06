package com.example.projetbackend.dto;

public class AuthResponseDTO {

    private final String accessToken;
    private final String type;
    private final long expiresIn;
    private final String email;
    private final String role;
    private final String nom;
    private final String prenom;

    public AuthResponseDTO(String accessToken, String type, long expiresIn, String email, String role, String nom, String prenom) {
        this.accessToken = accessToken;
        this.type = type;
        this.expiresIn = expiresIn;
        this.email = email;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getAccessToken() { return accessToken; }
    public String getType() { return type; }
    public long getExpiresIn() { return expiresIn; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
}