import { Component, OnInit, signal } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from '../../models/auth/login-request';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnInit {

  email = '';
  password = '';
  errorMessage = signal('');

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly location: Location
  ) {
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      return;
    }

    this.authService.getCurrentAuth()
      .subscribe({
        next: authInfo => {
          this.navigateByRole(authInfo.role);
        },
        error: () => {
          this.authService.logout();
        }
      });
  }

  login(): void {
    this.errorMessage.set('');

    const request: LoginRequest = {
      email: this.email,
      password: this.password
    };

    this.authService.login(request)
      .subscribe({
        next: response => {
          this.authService.saveToken(response.token);
          this.navigateByRole(response.role);
        },

        error: error => {
          if (error.error?.message === 'Email is not verified') {
            this.errorMessage.set('Email не подтверждён');
            return;
          }

          if (error.error?.message === 'Invalid email or password') {
            this.errorMessage.set('Неверный email или пароль');
            return;
          }

          this.errorMessage.set('Ошибка входа');
        }
      });
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  restoreAccount(): void {
    this.router.navigate(['/restore-account']);
  }

  goBack(): void {
    this.location.back();
  }

  private navigateByRole(role: string): void {
    if (role === 'SUPER_ADMIN') {
      this.router.navigate(['/super-admin']);
      return;
    }

    if (role === 'HOTEL_ADMIN') {
      this.router.navigate(['/hotel-admin']);
      return;
    }

    this.router.navigate(['/']);
  }
}
