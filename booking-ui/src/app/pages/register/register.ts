import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserCreate } from '../../models/user-create';
import { UserService } from '../../services/user';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit {

  errorMessage = signal('');
  successMessage = signal('');
  isLoading = signal(false);

  firstName = '';
  lastName = '';
  phone = '';
  email = '';
  registeredEmail = '';
  password = '';
  confirmPassword = '';
  initialBalance = 0;

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  register(): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (this.password !== this.confirmPassword) {
      this.errorMessage.set('Пароли не совпадают');
      return;
    }

    this.isLoading.set(true);

    const user: UserCreate = {
      firstName: this.firstName,
      lastName: this.lastName,
      phone: this.phone,
      email: this.email,
      password: this.password,
      initialBalance: this.initialBalance
    };

    this.userService.create(user).subscribe({
      next: () => {
        this.isLoading.set(false);

        this.successMessage.set(
          'Регистрация успешна. Проверьте email и подтвердите регистрацию.'
        );

        this.registeredEmail = this.email;

        this.firstName = '';
        this.lastName = '';
        this.phone = '';
        this.email = '';
        this.password = '';
        this.confirmPassword = '';
        this.initialBalance = 0;
      },

      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);

        this.errorMessage.set(
          error.error?.message ?? 'Registration failed'
        );
      }
    });
  }
}
