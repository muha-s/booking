import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../services/auth';

export const hotelAdminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    return router.createUrlTree(['/login']);
  }

  return authService.getCurrentAuth().pipe(
    map(authInfo => {
      if (authInfo.role === 'HOTEL_ADMIN') {
        return true;
      }

      return router.createUrlTree(['/']);
    }),

    catchError(() => {
      authService.logout();
      return of(router.createUrlTree(['/login']));
    })
  );
};
