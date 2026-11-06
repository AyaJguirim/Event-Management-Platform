import { Injectable, signal } from '@angular/core';
import { UtilisateurConnecte } from '../models/utilisateur-connecte';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private utilisateurSignal = signal<UtilisateurConnecte | null>(this.chargerDepuisSession());

  utilisateur = this.utilisateurSignal.asReadonly();

  private chargerDepuisSession(): UtilisateurConnecte | null {
    const email = sessionStorage.getItem('email');
    const role = sessionStorage.getItem('role');
    const nom = sessionStorage.getItem('nom');
    const prenom = sessionStorage.getItem('prenom');
    return email && role && nom && prenom ? { email, role, nom, prenom } : null;
  }

  connecter(email: string, role: string, token: string, nom: string, prenom: string): void {
    sessionStorage.setItem('token', token);
    sessionStorage.setItem('email', email);
    sessionStorage.setItem('role', role);
    sessionStorage.setItem('nom', nom);
    sessionStorage.setItem('prenom', prenom);
    this.utilisateurSignal.set({ email, role, nom, prenom });
  }

  deconnecter(): void {
    sessionStorage.clear();
    this.utilisateurSignal.set(null);
  }

  estConnecte(): boolean {
    return this.utilisateurSignal() !== null;
  }

  estAdmin(): boolean {
    return this.utilisateurSignal()?.role === 'ADMIN';
  }
}