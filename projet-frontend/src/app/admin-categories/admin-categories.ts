import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategorieService } from '../services/categorie-service';
import { CategorieResponseDTO } from '../models/categorie-response-dto';
import { CategorieRequestDTO } from '../models/categorie-request-dto';

@Component({
  selector: 'app-admin-categories',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-categories.html',
  styleUrl: './admin-categories.scss'
})
export class AdminCategories implements OnInit {
  categories = signal<CategorieResponseDTO[]>([]);
  enCours = signal(false);
  messageErreur = signal('');
  messageSucces = signal('');
  modeEdition = signal<number | null>(null);

  formulaire: FormGroup;

  constructor(private fb: FormBuilder, private categorieService: CategorieService) {
    this.formulaire = this.fb.group({
      nom: ['', [Validators.required]],
      description: ['']
    });
  }

  get nom() { return this.formulaire.get('nom'); }

  ngOnInit(): void { this.charger(); }

  charger(): void {
    this.enCours.set(true);
    this.categorieService.getAll().subscribe({
      next: (data) => { this.categories.set(data); this.enCours.set(false); },
      error: () => { this.messageErreur.set('Impossible de charger les catégories.'); this.enCours.set(false); }
    });
  }

  ouvrirAjout(): void {
    this.modeEdition.set(-1);
    this.formulaire.reset({ nom: '', description: '' });
    this.messageErreur.set('');
  }

  ouvrirEdition(categorie: CategorieResponseDTO): void {
    this.modeEdition.set(categorie.id);
    this.formulaire.setValue({ nom: categorie.nom, description: categorie.description });
    this.messageErreur.set('');
  }

  annuler(): void { this.modeEdition.set(null); }

  enregistrer(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const dto: CategorieRequestDTO = this.formulaire.value;
    const id = this.modeEdition();
    const requete = id === -1 ? this.categorieService.ajouter(dto) : this.categorieService.modifier(id!, dto);

    requete.subscribe({
      next: () => {
        this.modeEdition.set(null);
        this.messageSucces.set('Catégorie enregistrée.');
        this.charger();
        setTimeout(() => this.messageSucces.set(''), 3000);
      },
      error: () => this.messageErreur.set('Une erreur est survenue.')
    });
  }

  supprimer(categorie: CategorieResponseDTO): void {
    if (!confirm(`Supprimer la catégorie "${categorie.nom}" ?`)) return;

    this.categorieService.supprimer(categorie.id).subscribe({
      next: () => this.charger(),
      error: (err: any) => {
        this.messageErreur.set(err.status === 409
          ? 'Cette catégorie est utilisée par au moins un événement et ne peut pas être supprimée.'
          : 'Impossible de supprimer cette catégorie.');
        setTimeout(() => this.messageErreur.set(''), 4000);
      }
    });
  }
}