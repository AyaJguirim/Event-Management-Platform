package com.example.projetbackend.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int statut;
    private String message;

    public ErrorResponseDTO(int statut, String message) {
        this.timestamp = LocalDateTime.now();
        this.statut = statut;
        this.message = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatut() { return statut; }
    public String getMessage() { return message; }
}