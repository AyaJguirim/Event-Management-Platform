import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategorieResponseDTO } from '../models/categorie-response-dto';
import { CategorieRequestDTO } from '../models/categorie-request-dto';

@Injectable({ providedIn: 'root' })
export class CategorieService {
  private readonly baseUrl = 'http://localhost:8082/api/categories';

  constructor(private http: HttpClient) {}

  getAll(): Observable<CategorieResponseDTO[]> {
    return this.http.get<CategorieResponseDTO[]>(this.baseUrl);
  }

  ajouter(dto: CategorieRequestDTO): Observable<CategorieResponseDTO> {
    return this.http.post<CategorieResponseDTO>(this.baseUrl, dto);
  }

  modifier(id: number, dto: CategorieRequestDTO): Observable<CategorieResponseDTO> {
    return this.http.put<CategorieResponseDTO>(`${this.baseUrl}/${id}`, dto);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}