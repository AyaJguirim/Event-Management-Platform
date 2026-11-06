package com.example.projetbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResendVerificationRequestDTO {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Le format de l'email est invalide")
    private String email;

    public ResendVerificationRequestDTO() {
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}