import { Routes } from '@angular/router';
import { Accueil } from './accueil/accueil';
import { Register } from './register/register';
import { Login } from './login/login';
import { Evenements } from './evenements/evenements';
import { EvenementDetail } from './evenement-detail/evenement-detail';
import { MesInscriptions } from './mes-inscriptions/mes-inscriptions';
import { Admin } from './admin/admin';
import { AdminCategories } from './admin-categories/admin-categories';
import { AdminEvenements } from './admin-evenements/admin-evenements';
import { AdminInscriptions } from './admin-inscriptions/admin-inscriptions';
import { authGuard } from './guards/auth-guard';
import { adminGuard } from './guards/admin-guard';
import { VerifyEmail } from './verify-email/verify-email';

export const routes: Routes = [
  { path: '', component: Accueil },
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'evenements', component: Evenements, canActivate: [authGuard] },
  { path: 'evenements/:id', component: EvenementDetail, canActivate: [authGuard] },
  { path: 'mes-inscriptions', component: MesInscriptions, canActivate: [authGuard] },
  { path: 'verify-email', component: VerifyEmail },
  {
    path: 'admin',
    component: Admin,
    canActivate: [adminGuard],
    children: [
      { path: '', redirectTo: 'categories', pathMatch: 'full' },
      { path: 'categories', component: AdminCategories },
      { path: 'evenements', component: AdminEvenements },
      { path: 'inscriptions', component: AdminInscriptions },
    ]
  },
];