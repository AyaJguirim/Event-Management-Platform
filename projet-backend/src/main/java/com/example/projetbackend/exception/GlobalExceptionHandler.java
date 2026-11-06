package com.example.projetbackend.exception;

import com.example.projetbackend.dto.ErrorResponseDTO;
import com.example.projetbackend.dto.ValidationErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erreurs de validation @Valid (ex. RegisterRequestDTO) - un message par champ invalide
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erreur ->
                erreurs.put(erreur.getField(), erreur.getDefaultMessage())
        );

        ValidationErrorResponseDTO reponse = new ValidationErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Des erreurs de validation ont été détectées",
                erreurs
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse);
    }

    // Identifiants invalides lors du login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponseDTO reponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Email ou mot de passe incorrect"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(reponse);
    }

    // Toutes les exceptions métier custom (@ResponseStatus) : ResourceNotFound, AccesRefuse,
    // EmailDejaUtilise, RessourceUtilisee, VerificationTokenInvalid, VerificationTokenExpired
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        HttpStatus statut = extraireStatutHttp(ex);

        ErrorResponseDTO reponse = new ErrorResponseDTO(statut.value(), ex.getMessage());
        return ResponseEntity.status(statut).body(reponse);
    }

    private HttpStatus extraireStatutHttp(RuntimeException ex) {
        org.springframework.web.bind.annotation.ResponseStatus annotation =
                ex.getClass().getAnnotation(org.springframework.web.bind.annotation.ResponseStatus.class);

        if (annotation != null) {
            return annotation.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}