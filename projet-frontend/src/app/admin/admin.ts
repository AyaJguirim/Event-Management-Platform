import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthStateService } from '../services/auth-state-service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './admin.html',
  styleUrl: './admin.scss'
})
export class Admin {
  utilisateur;

  constructor(private authState: AuthStateService, private router: Router) {
    this.utilisateur = this.authState.utilisateur;
  }

  deconnexion(): void {
    this.authState.deconnecter();
    this.router.navigate(['/login']);
  }
}