package com.example.projetbackend.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponseDTO {
    private LocalDateTime timestamp;
    private int statut;
    private String message;
    private Map<String, String> erreursChamps;

    public ValidationErrorResponseDTO(int statut, String message, Map<String, String> erreursChamps) {
        this.timestamp = LocalDateTime.now();
        this.statut = statut;
        this.message = message;
        this.erreursChamps = erreursChamps;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatut() { return statut; }
    public String getMessage() { return message; }
    public Map<String, String> getErreursChamps() { return erreursChamps; }
}