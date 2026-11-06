import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EvenementService } from '../services/evenement-service';
import { CategorieService } from '../services/categorie-service';
import { AuthStateService } from '../services/auth-state-service';
import { EvenementResponseDTO } from '../models/evenement-response-dto';
import { CategorieResponseDTO } from '../models/categorie-response-dto';

@Component({
  selector: 'app-evenements',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './evenements.html',
  styleUrl: './evenements.scss'
})
export class Evenements implements OnInit {
  evenements = signal<EvenementResponseDTO[]>([]);
  categories = signal<CategorieResponseDTO[]>([]);
  enCours = signal(false);
  messageErreur = signal('');

  titreRecherche = '';
  categorieSelectionnee: number | null = null;
  dateDebut = '';
  dateFin = '';

  constructor(
    private evenementService: EvenementService,
    private categorieService: CategorieService,
    private authState: AuthStateService,
    private router: Router
  ) {}
  
  get utilisateur() {
    return this.authState.utilisateur;
  }

  ngOnInit(): void {
    this.chargerCategories();
    this.chargerTout();
  }

  chargerCategories(): void {
    this.categorieService.getAll().subscribe({ next: (data) => this.categories.set(data) });
  }

  chargerTout(): void {
    this.enCours.set(true);
    this.messageErreur.set('');
    this.evenementService.getAll().subscribe({
      next: (data) => { this.evenements.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Impossible de charger les événements.'); this.enCours.set(false); }
    });
  }

  rechercherParTitre(): void {
    if (!this.titreRecherche.trim()) { this.chargerTout(); return; }
    this.enCours.set(true);
    this.evenementService.rechercherParTitre(this.titreRecherche.trim()).subscribe({
      next: (data) => { this.evenements.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Erreur lors de la recherche.'); this.enCours.set(false); }
    });
  }

  filtrerParCategorie(): void {
    if (this.categorieSelectionnee === null) { this.chargerTout(); return; }
    this.enCours.set(true);
    this.evenementService.filtrerParCategorie(this.categorieSelectionnee).subscribe({
      next: (data) => { this.evenements.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Erreur lors du filtrage.'); this.enCours.set(false); }
    });
  }

  filtrerParDate(): void {
    if (!this.dateDebut || !this.dateFin) return;
    this.enCours.set(true);
    this.evenementService.filtrerParDate(`${this.dateDebut}T00:00:00`, `${this.dateFin}T23:59:59`).subscribe({
      next: (data) => { this.evenements.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Erreur lors du filtrage par date.'); this.enCours.set(false); }
    });
  }

  reinitialiser(): void {
    this.titreRecherche = '';
    this.categorieSelectionnee = null;
    this.dateDebut = '';
    this.dateFin = '';
    this.chargerTout();
  }

  deconnexion(): void {
    this.authState.deconnecter();
    this.router.navigate(['/login']);
  }
}