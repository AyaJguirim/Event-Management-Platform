package com.example.projetbackend.dto;

import jakarta.validation.constraints.NotNull;

public class InscriptionRequestDTO {

    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    private Long evenementId;

    public InscriptionRequestDTO() {
    }

    public Long getEvenementId() { return evenementId; }
    public void setEvenementId(Long evenementId) { this.evenementId = evenementId; }
}