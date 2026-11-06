import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterRequestDTO } from '../models/register-request-dto';
import { UtilisateurResponseDTO } from '../models/utilisateur-response-dto';
import { LoginRequestDTO } from '../models/login-request-dto';
import { AuthResponseDTO } from '../models/auth-response-dto';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = 'http://localhost:8082/api/auth';

  constructor(private http: HttpClient) {}

  register(donnees: RegisterRequestDTO): Observable<UtilisateurResponseDTO> {
    return this.http.post<UtilisateurResponseDTO>(`${this.baseUrl}/register`, donnees);
  }

  login(donnees: LoginRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${this.baseUrl}/login`, donnees);
  }

  verifyEmail(token: string): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}/verify-email`, { params: { token } });
  }

  resendVerification(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/resend-verification`, { email });
  }
}