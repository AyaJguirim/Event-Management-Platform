package com.example.projetbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VerificationTokenInvalidException extends RuntimeException {
    public VerificationTokenInvalidException(String message) {
        super(message);
    }
}