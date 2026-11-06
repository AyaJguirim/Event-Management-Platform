package com.example.projetbackend.dto;

import com.example.projetbackend.entity.EtatInscription;

public class ChangerEtatRequestDTO {
    private EtatInscription etat;

    public ChangerEtatRequestDTO() {
    }

    public EtatInscription getEtat() { return etat; }

    public void setEtat(EtatInscription etat) { this.etat = etat; }
}