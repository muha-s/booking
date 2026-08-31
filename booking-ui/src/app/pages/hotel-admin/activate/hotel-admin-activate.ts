import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelAdminService } from '../../../services/hotel-admin/hotel-admin';

@Component({
  selector: 'app-hotel-admin-activate',
  imports: [FormsModule],
  templateUrl: './hotel-admin-activate.html',
  styleUrl: './hotel-admin-activate.css'
})
export class HotelAdminActivate {

  password = '';
  confirmPassword = '';

  errorMessage = signal('');
  successMessage = signal('');
  loading = signal(false);

  private readonly token: string;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly hotelAdminService: HotelAdminService
  ) {
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
  }

  activate(): void {
    if (this.loading()) {
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');

    if (!this.token) {
      this.errorMessage.set('Ссылка активации недействительна');
      return;
    }

    if (this.password.length < 8) {
      this.errorMessage.set('Пароль должен содержать не менее 8 символов');
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage.set('Пароли не совпадают');
      return;
    }

    this.loading.set(true);

    this.hotelAdminService.activate({
      token: this.token,
      password: this.password
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set(
          'Аккаунт успешно активирован. Теперь вы можете войти в панель управления.'
        );
      },

      error: error => {
        this.loading.set(false);

        this.errorMessage.set(
          error.error?.message ?? 'Не удалось активировать аккаунт'
        );
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }
}
