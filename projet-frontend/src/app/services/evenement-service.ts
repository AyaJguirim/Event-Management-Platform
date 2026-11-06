import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EvenementResponseDTO } from '../models/evenement-response-dto';
import { EvenementRequestDTO } from '../models/evenement-request-dto';

@Injectable({ providedIn: 'root' })
export class EvenementService {
  private readonly baseUrl = 'http://localhost:8082/api/evenements';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EvenementResponseDTO[]> {
    return this.http.get<EvenementResponseDTO[]>(this.baseUrl);
  }

  getById(id: number): Observable<EvenementResponseDTO> {
    return this.http.get<EvenementResponseDTO>(`${this.baseUrl}/${id}`);
  }

  rechercherParTitre(titre: string): Observable<EvenementResponseDTO[]> {
    const params = new HttpParams().set('titre', titre);
    return this.http.get<EvenementResponseDTO[]>(`${this.baseUrl}/recherche`, { params });
  }

  filtrerParCategorie(categorieId: number): Observable<EvenementResponseDTO[]> {
    return this.http.get<EvenementResponseDTO[]>(`${this.baseUrl}/categorie/${categorieId}`);
  }

  filtrerParDate(debut: string, fin: string): Observable<EvenementResponseDTO[]> {
    const params = new HttpParams().set('debut', debut).set('fin', fin);
    return this.http.get<EvenementResponseDTO[]>(`${this.baseUrl}/date`, { params });
  }

  ajouter(dto: EvenementRequestDTO): Observable<EvenementResponseDTO> {
    return this.http.post<EvenementResponseDTO>(this.baseUrl, dto);
  }

  modifier(id: number, dto: EvenementRequestDTO): Observable<EvenementResponseDTO> {
    return this.http.put<EvenementResponseDTO>(`${this.baseUrl}/${id}`, dto);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}