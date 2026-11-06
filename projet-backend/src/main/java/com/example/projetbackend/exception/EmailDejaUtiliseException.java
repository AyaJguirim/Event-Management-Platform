package com.example.projetbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailDejaUtiliseException extends RuntimeException {
    public EmailDejaUtiliseException(String message) {
        super(message);
    }
}