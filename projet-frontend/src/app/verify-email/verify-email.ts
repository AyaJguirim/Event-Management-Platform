import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.scss'
})
export class VerifyEmail implements OnInit {
  enCours = signal(true);
  succes = signal(false);
  messageErreur = signal('');

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.enCours.set(false);
      this.messageErreur.set('Lien de vérification invalide.');
      return;
    }

    this.authService.verifyEmail(token).subscribe({
      next: () => {
        this.enCours.set(false);
        this.succes.set(true);
      },
      error: (err: any) => {
        this.enCours.set(false);
        this.messageErreur.set(err.status === 410
          ? 'Ce lien de vérification a expiré. Demandez-en un nouveau depuis la page de connexion.'
          : 'Ce lien de vérification est invalide ou a déjà été utilisé.');
      }
    });
  }
}