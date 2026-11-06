package com.example.projetbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String adresseExpediteur;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from}") String adresseExpediteur) {
        this.mailSender = mailSender;
        this.adresseExpediteur = adresseExpediteur;
    }

    public void envoyerEmailVerification(String destinataire, String nomUtilisateur, String lienVerification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adresseExpediteur);
        message.setTo(destinataire);
        message.setSubject("Vérification de votre adresse email");
        message.setText("Bonjour " + nomUtilisateur + ",\n\nVotre compte a été créé avec succès.\nCliquez sur le lien suivant pour vérifier votre adresse email :\n\n" + lienVerification + "\n\nCe lien est temporaire.\n\nL'équipe du projet.");
        mailSender.send(message);
    }
}