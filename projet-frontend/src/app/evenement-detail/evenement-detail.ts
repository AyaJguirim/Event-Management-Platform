import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EvenementService } from '../services/evenement-service';
import { InscriptionService } from '../services/inscription-service';
import { EvenementResponseDTO } from '../models/evenement-response-dto';

@Component({
  selector: 'app-evenement-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './evenement-detail.html',
  styleUrl: './evenement-detail.scss'
})
export class EvenementDetail implements OnInit {
  evenement = signal<EvenementResponseDTO | null>(null);
  enCours = signal(true);
  inscriptionEnCours = signal(false);
  messageErreur = signal('');
  messageSucces = signal('');

  constructor(
    private route: ActivatedRoute,
    private evenementService: EvenementService,
    private inscriptionService: InscriptionService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.evenementService.getById(id).subscribe({
      next: (data) => { this.evenement.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Événement introuvable.'); this.enCours.set(false); }
    });
  }

  sInscrire(): void {
    const evt = this.evenement();
    if (!evt) return;

    this.inscriptionEnCours.set(true);
    this.messageErreur.set('');
    this.messageSucces.set('');

    this.inscriptionService.sInscrire({ evenementId: evt.id }).subscribe({
      next: () => {
        this.inscriptionEnCours.set(false);
        this.messageSucces.set('Inscription enregistrée ! Retrouvez-la dans "Mes inscriptions".');
      },
      error: (err: any) => {
        this.inscriptionEnCours.set(false);
        this.messageErreur.set(err.status === 409
          ? 'Vous êtes déjà inscrit à cet événement, ou la capacité maximale est atteinte.'
          : "Impossible de finaliser l'inscription. Réessayez.");
      }
    });
  }
}