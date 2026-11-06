import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InscriptionService } from '../services/inscription-service';
import { InscriptionResponseDTO } from '../models/inscription-response-dto';

@Component({
  selector: 'app-admin-inscriptions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-inscriptions.html',
  styleUrl: './admin-inscriptions.scss'
})
export class AdminInscriptions implements OnInit {
  inscriptions = signal<InscriptionResponseDTO[]>([]);
  enCours = signal(true);
  messageErreur = signal('');
  modificationEnCoursId = signal<number | null>(null);

  etatsPossibles = ['EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'TERMINEE'];

  constructor(private inscriptionService: InscriptionService) {}

  ngOnInit(): void { this.charger(); }

  charger(): void {
    this.enCours.set(true);
    this.inscriptionService.getAll().subscribe({
      next: (data) => { this.inscriptions.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Impossible de charger les inscriptions.'); this.enCours.set(false); }
    });
  }

  changerEtat(inscription: InscriptionResponseDTO, nouvelEtat: string): void {
    if (nouvelEtat === inscription.etat) return;

    this.modificationEnCoursId.set(inscription.id);
    this.inscriptionService.changerEtat(inscription.id, nouvelEtat).subscribe({
      next: () => { this.modificationEnCoursId.set(null); this.charger(); },
      error: () => {
        this.modificationEnCoursId.set(null);
        this.messageErreur.set(`Transition de "${this.libelleEtat(inscription.etat)}" vers "${this.libelleEtat(nouvelEtat)}" non autorisée.`);
        this.charger(); // resynchronise le select sur l'état réel
        setTimeout(() => this.messageErreur.set(''), 4000);
      }
    });
  }

  libelleEtat(etat: string): string {
    const libelles: Record<string, string> = {
      EN_ATTENTE: 'En attente', CONFIRMEE: 'Confirmée', ANNULEE: 'Annulée', TERMINEE: 'Terminée'
    };
    return libelles[etat] ?? etat;
  }
}