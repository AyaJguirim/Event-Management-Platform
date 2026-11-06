import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthStateService } from '../services/auth-state-service';

export const adminGuard: CanActivateFn = () => {
  const authState = inject(AuthStateService);
  const router = inject(Router);

  if (authState.estConnecte() && authState.estAdmin()) {
    return true;
  }
  router.navigate(['/login']);
  return false;
};