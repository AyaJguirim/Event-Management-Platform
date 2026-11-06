package com.example.projetbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RessourceUtiliseeException extends RuntimeException {
    public RessourceUtiliseeException(String message) {
        super(message);
    }
}