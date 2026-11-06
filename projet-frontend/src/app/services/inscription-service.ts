import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InscriptionRequestDTO } from '../models/inscription-request-dto';
import { InscriptionResponseDTO } from '../models/inscription-response-dto';
import { ChangerEtatRequestDTO } from '../models/changer-etat-request-dto';

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  private readonly baseUrl = 'http://localhost:8082/api/inscriptions';

  constructor(private http: HttpClient) {}

  sInscrire(dto: InscriptionRequestDTO): Observable<InscriptionResponseDTO> {
    return this.http.post<InscriptionResponseDTO>(this.baseUrl, dto);
  }

  mesInscriptions(): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(`${this.baseUrl}/mes-inscriptions`);
  }

  annuler(id: number): Observable<InscriptionResponseDTO> {
    return this.http.put<InscriptionResponseDTO>(`${this.baseUrl}/${id}/annuler`, {});
  }

  getAll(): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(this.baseUrl);
  }

  changerEtat(id: number, etat: string): Observable<InscriptionResponseDTO> {
    const dto: ChangerEtatRequestDTO = { etat };
    return this.http.put<InscriptionResponseDTO>(`${this.baseUrl}/${id}/etat`, dto);
  }
}