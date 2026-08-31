import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserRestore } from '../../models/user/user-restore';
import { UserRestoreRequest } from '../../models/user/user-restore-request';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-restore-account',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './restore-account.html',
  styleUrl: './restore-account.css'
})
export class RestoreAccount implements OnInit {

  email = '';
  password = '';
  confirmPassword = '';

  token = '';

  loading = signal(false);
  successMessage = signal('');
  errorMessage = signal('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly userService: UserService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
  }

  requestRestore(): void {
    if (this.loading()) {
      return;
    }

    if (!this.email.trim()) {
      this.errorMessage.set('Введите email');
      return;
    }

    const request: UserRestoreRequest = {
      email: this.email.trim()
    };

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.userService.requestRestore(request).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set(
          'Письмо со ссылкой для восстановления отправлено на ваш email'
        );
      },

      error: () => {
        this.loading.set(false);
        this.errorMessage.set(
          'Не удалось отправить письмо для восстановления аккаунта'
        );
      }
    });
  }

  restore(): void {
    if (this.loading()) {
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

    const restore: UserRestore = {
      token: this.token,
      password: this.password
    };

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.userService.restore(restore).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set(
          'Аккаунт восстановлен. Теперь вы можете войти с новым паролем'
        );
      },

      error: () => {
        this.loading.set(false);
        this.errorMessage.set(
          'Не удалось восстановить аккаунт. Ссылка недействительна или устарела'
        );
      }
    });
  }
}
