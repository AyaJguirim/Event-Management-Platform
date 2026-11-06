import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  formulaire: FormGroup;
  enCours = signal(false);
  messageErreur = signal('');
  messageSucces = signal('');

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.formulaire = this.fb.group({
      nom: ['', [Validators.required]],
      prenom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get nom() { return this.formulaire.get('nom'); }
  get prenom() { return this.formulaire.get('prenom'); }
  get email() { return this.formulaire.get('email'); }
  get motDePasse() { return this.formulaire.get('motDePasse'); }

  soumettre(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    this.enCours.set(true);
    this.messageErreur.set('');
    this.messageSucces.set('');

    this.authService.register(this.formulaire.value).subscribe({
      next: () => {
        this.enCours.set(false);
        this.messageSucces.set('Compte créé ! Vérifiez votre boîte mail pour valider votre adresse email avant de vous connecter.');
        this.formulaire.reset();
      },
      error: (err: any) => {
        this.enCours.set(false);
        if (err.status === 409) {
          this.messageErreur.set('Cette adresse email est déjà utilisée.');
        } else if (err.status === 400 && err.error?.erreursChamps) {
          const erreurs = Object.values(err.error.erreursChamps) as string[];
          this.messageErreur.set(erreurs.join(' '));
        } else {
          this.messageErreur.set('Une erreur est survenue. Réessayez.');
        }
      }
    });
  }
}