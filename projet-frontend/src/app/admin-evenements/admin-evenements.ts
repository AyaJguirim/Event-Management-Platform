import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EvenementService } from '../services/evenement-service';
import { CategorieService } from '../services/categorie-service';
import { EvenementResponseDTO } from '../models/evenement-response-dto';
import { EvenementRequestDTO } from '../models/evenement-request-dto';
import { CategorieResponseDTO } from '../models/categorie-response-dto';

@Component({
  selector: 'app-admin-evenements',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-evenements.html',
  styleUrl: './admin-evenements.scss'
})
export class AdminEvenements implements OnInit {
  evenements = signal<EvenementResponseDTO[]>([]);
  categories = signal<CategorieResponseDTO[]>([]);
  enCours = signal(false);
  messageErreur = signal('');
  messageSucces = signal('');
  modeEdition = signal<number | null>(null);

  formulaire: FormGroup;

  constructor(
    private fb: FormBuilder,
    private evenementService: EvenementService,
    private categorieService: CategorieService
  ) {
    this.formulaire = this.fb.group({
      titre: ['', [Validators.required]],
      description: [''],
      date: ['', [Validators.required]],
      lieu: ['', [Validators.required]],
      capacite: [1, [Validators.required, Validators.min(1)]],
      prix: [0, [Validators.required, Validators.min(0)]],
      categorieId: [0, [Validators.required]],
      actif: [true]
    });
  }

  get titre() { return this.formulaire.get('titre'); }
  get date() { return this.formulaire.get('date'); }
  get lieu() { return this.formulaire.get('lieu'); }
  get capacite() { return this.formulaire.get('capacite'); }
  get categorieId() { return this.formulaire.get('categorieId'); }

  ngOnInit(): void {
    this.categorieService.getAll().subscribe({ next: (data) => this.categories.set(data) });
    this.charger();
  }

  charger(): void {
    this.enCours.set(true);
    this.evenementService.getAll().subscribe({
      next: (data) => { this.evenements.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Impossible de charger les événements.'); this.enCours.set(false); }
    });
  }

  ouvrirAjout(): void {
    this.modeEdition.set(-1);
    this.formulaire.reset({ titre: '', description: '', date: '', lieu: '', capacite: 1, prix: 0, categorieId: 0, actif: true });
    this.messageErreur.set('');
  }

  ouvrirEdition(evenement: EvenementResponseDTO): void {
    this.modeEdition.set(evenement.id);
    this.formulaire.setValue({
      titre: evenement.titre,
      description: evenement.description,
      date: evenement.date.substring(0, 16),
      lieu: evenement.lieu,
      capacite: evenement.capacite,
      prix: evenement.prix,
      categorieId: evenement.categorieId,
      actif: evenement.actif
    });
    this.messageErreur.set('');
  }

  annuler(): void { this.modeEdition.set(null); }

  enregistrer(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const valeurs = this.formulaire.value;
    const dto: EvenementRequestDTO = {
      ...valeurs,
      date: valeurs.date.length === 16 ? `${valeurs.date}:00` : valeurs.date
    };

    const id = this.modeEdition();
    const requete = id === -1 ? this.evenementService.ajouter(dto) : this.evenementService.modifier(id!, dto);

    requete.subscribe({
      next: () => {
        this.modeEdition.set(null);
        this.messageSucces.set('Événement enregistré.');
        this.charger();
        setTimeout(() => this.messageSucces.set(''), 3000);
      },
      error: () => this.messageErreur.set('Une erreur est survenue. Vérifiez les champs.')
    });
  }

  basculerActif(evenement: EvenementResponseDTO): void {
    const dto: EvenementRequestDTO = {
      titre: evenement.titre, description: evenement.description, date: evenement.date,
      lieu: evenement.lieu, capacite: evenement.capacite, actif: !evenement.actif,
      prix: evenement.prix, categorieId: evenement.categorieId
    };
    this.evenementService.modifier(evenement.id, dto).subscribe({
      next: () => this.charger(),
      error: () => this.messageErreur.set("Impossible de changer l'état de l'événement.")
    });
  }

  supprimer(evenement: EvenementResponseDTO): void {
    if (!confirm(`Supprimer l'événement "${evenement.titre}" ?`)) return;

    this.evenementService.supprimer(evenement.id).subscribe({
      next: () => this.charger(),
      error: (err: any) => {
        this.messageErreur.set(err.status === 409
          ? 'Cet événement a déjà des inscriptions et ne peut pas être supprimé.'
          : 'Impossible de supprimer cet événement.');
        setTimeout(() => this.messageErreur.set(''), 4000);
      }
    });
  }
}