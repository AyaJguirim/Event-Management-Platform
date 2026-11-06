import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { AuthStateService } from '../services/auth-state-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  formulaire: FormGroup;
  enCours = signal(false);
  messageErreur = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private authState: AuthStateService,
    private router: Router
  ) {
    this.formulaire = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required]]
    });
  }

  get email() { return this.formulaire.get('email'); }
  get motDePasse() { return this.formulaire.get('motDePasse'); }

  soumettre(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    this.enCours.set(true);
    this.messageErreur.set('');

    this.authService.login(this.formulaire.value).subscribe({
      next: (reponse) => {
        this.authState.connecter(reponse.email, reponse.role, reponse.accessToken, reponse.nom, reponse.prenom);
        this.enCours.set(false);
        this.router.navigate([reponse.role === 'ADMIN' ? '/admin' : '/evenements']);
      },
      error: (err: any) => {
        this.enCours.set(false);
        this.messageErreur.set(err.status === 401
          ? 'Email ou mot de passe incorrect, ou email non vérifié.'
          : 'Une erreur est survenue. Réessayez.');
      }
    });
  }

  messageRenvoiSucces = signal('');
renvoiEnCours = signal(false);

renvoyerEmail(): void {
  const email = this.formulaire.get('email')?.value;
  if (!email) return;

  this.renvoiEnCours.set(true);
  this.authService.resendVerification(email).subscribe({
    next: () => {
      this.renvoiEnCours.set(false);
      this.messageErreur.set('');
      this.messageRenvoiSucces.set('Un nouvel email de vérification a été envoyé.');
    },
    error: () => {
      this.renvoiEnCours.set(false);
      this.messageErreur.set("Impossible de renvoyer l'email. Vérifiez l'adresse saisie.");
    }
  });
}
}