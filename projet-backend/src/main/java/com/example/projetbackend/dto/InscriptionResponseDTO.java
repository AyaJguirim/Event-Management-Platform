package com.example.projetbackend.dto;

import com.example.projetbackend.entity.EtatInscription;
import java.time.LocalDateTime;

public class InscriptionResponseDTO {
    private Long id;
    private LocalDateTime dateInscription;
    private EtatInscription etat;
    private Long utilisateurId;
    private String utilisateurNomComplet;
    private Long evenementId;
    private String evenementTitre;

    public InscriptionResponseDTO() {
    }

    public Long getId() { return id; }

    public LocalDateTime getDateInscription() { return dateInscription; }

    public EtatInscription getEtat() { return etat; }

    public Long getUtilisateurId() { return utilisateurId; }

    public String getUtilisateurNomComplet() { return utilisateurNomComplet; }

    public Long getEvenementId() { return evenementId; }

    public String getEvenementTitre() { return evenementTitre; }

    public void setId(Long id) { this.id = id; }

    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }

    public void setEtat(EtatInscription etat) { this.etat = etat; }

    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public void setUtilisateurNomComplet(String utilisateurNomComplet) { this.utilisateurNomComplet = utilisateurNomComplet; }

    public void setEvenementId(Long evenementId) { this.evenementId = evenementId; }

    public void setEvenementTitre(String evenementTitre) { this.evenementTitre = evenementTitre; }
}