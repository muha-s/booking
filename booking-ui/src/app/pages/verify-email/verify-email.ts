import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserService } from '../../services/user';


@Component({
  selector: 'app-verify-email',
  imports: [RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css'

})
export class VerifyEmail implements OnInit {

  successMessage = signal('');
  errorMessage = signal('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly userService: UserService
  ) {
  }

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token') ?? '';

    if (!token) {
      this.errorMessage.set('Токен подтверждения отсутствует');
      return;
    }

    this.userService.verifyEmail(token)
      .subscribe({
        next: () => {
          this.successMessage.set('Email успешно подтверждён');
        },
        error: error => {
          const message = error.error?.message;

          if (message === 'Invalid verification token') {
            this.errorMessage.set('Недействительная ссылка подтверждения');
            return;
          }
          if (message === 'Verification token has expired') {
            this.errorMessage.set('Срок действия ссылки подтверждения истёк');
            return;
          }
          this.errorMessage.set('Не удалось подтвердить email');
        }
      });
  }
}
