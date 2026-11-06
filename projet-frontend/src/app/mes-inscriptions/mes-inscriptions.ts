import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { InscriptionService } from '../services/inscription-service';
import { InscriptionResponseDTO } from '../models/inscription-response-dto';

@Component({
  selector: 'app-mes-inscriptions',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mes-inscriptions.html',
  styleUrl: './mes-inscriptions.scss'
})
export class MesInscriptions implements OnInit {
  inscriptions = signal<InscriptionResponseDTO[]>([]);
  enCours = signal(true);
  messageErreur = signal('');
  annulationEnCoursId = signal<number | null>(null);

  constructor(private inscriptionService: InscriptionService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.enCours.set(true);
    this.inscriptionService.mesInscriptions().subscribe({
      next: (data) => { this.inscriptions.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Impossible de charger vos inscriptions.'); this.enCours.set(false); }
    });
  }

  annuler(id: number): void {
    this.annulationEnCoursId.set(id);
    this.inscriptionService.annuler(id).subscribe({
      next: () => { this.annulationEnCoursId.set(null); this.charger(); },
      error: () => { this.annulationEnCoursId.set(null); this.messageErreur.set("Impossible d'annuler cette inscription."); }
    });
  }

  peutAnnuler(etat: string): boolean {
    return etat === 'EN_ATTENTE' || etat === 'CONFIRMEE';
  }

  libelleEtat(etat: string): string {
    const libelles: Record<string, string> = {
      EN_ATTENTE: 'En attente', CONFIRMEE: 'Confirmée', ANNULEE: 'Annulée', TERMINEE: 'Terminée'
    };
    return libelles[etat] ?? etat;
  }
}