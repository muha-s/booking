import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.html',
  styleUrl: './super-admin.css',
  imports: [RouterLink],
})
export class SuperAdmin {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
