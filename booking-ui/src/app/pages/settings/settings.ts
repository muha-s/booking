import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user';
import { AuthService } from '../../services/auth';
import { UserProfile } from '../../models/user-profile';
import { UserUpdate } from '../../models/user-update';
import { UserPasswordUpdate } from '../../models/user-password-update';
import { UserEmailUpdate } from '../../models/user-email-update';

type SettingsSection = 'menu' | 'profile' | 'email' | 'password' | 'delete';

@Component({
  selector: 'app-settings',
  imports: [FormsModule, RouterLink],
  templateUrl: './settings.html',
  styleUrl: './settings.css'
})
export class Settings implements OnInit {

  userProfile = signal<UserProfile | null>(null);
  section = signal<SettingsSection>('menu');

  profileError = signal('');
  profileSaved = signal(false);

  passwordError = signal('');
  passwordSaved = signal(false);

  emailError = signal('');
  emailSaved = signal(false);
  emailLoading = signal(false);

  deleteError = signal('');

  firstName = '';
  lastName = '';
  phone = '';
  email = '';

  currentPassword = '';
  newPassword = '';
  confirmPassword = '';

  private originalFirstName = '';
  private originalLastName = '';
  private originalPhone = '';

  constructor(
    private readonly userService: UserService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  private loadProfile(): void {
    this.userService.findProfile().subscribe({
      next: profile => {
        this.userProfile.set(profile);

        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.phone = profile.phone;

        this.originalFirstName = profile.firstName;
        this.originalLastName = profile.lastName;
        this.originalPhone = profile.phone;
      }
    });
  }

  openSection(section: SettingsSection): void {
    this.clearMessages();

    if (section === 'email') {
      this.email = '';
    }

    if (section === 'password') {
      this.currentPassword = '';
      this.newPassword = '';
      this.confirmPassword = '';
    }

    this.section.set(section);
  }

  backToMenu(): void {
    this.clearMessages();

    const profile = this.userProfile();

    if (profile !== null) {
      this.firstName = profile.firstName;
      this.lastName = profile.lastName;
      this.phone = profile.phone;
    }

    this.email = '';
    this.currentPassword = '';
    this.newPassword = '';
    this.confirmPassword = '';

    this.section.set('menu');
  }

  private clearMessages(): void {
    this.profileError.set('');
    this.passwordError.set('');
    this.emailError.set('');
    this.deleteError.set('');
  }

  profileChanged(): boolean {
    return this.firstName !== this.originalFirstName
      || this.lastName !== this.originalLastName
      || this.phone !== this.originalPhone;
  }

  passwordFormFilled(): boolean {
    return this.currentPassword.trim() !== ''
      && this.newPassword.trim() !== ''
      && this.confirmPassword.trim() !== '';
  }

  emailFormFilled(): boolean {
    return this.email.trim() !== '';
  }

  updateProfile(): void {
    this.profileError.set('');

    const user: UserUpdate = {
      firstName: this.firstName,
      lastName: this.lastName,
      phone: this.phone
    };

    this.userService.updateProfile(user).subscribe({
      next: profile => {
        this.userProfile.set(profile);

        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.phone = profile.phone;

        this.originalFirstName = profile.firstName;
        this.originalLastName = profile.lastName;
        this.originalPhone = profile.phone;

        this.profileSaved.set(true);
      },
      error: error => {
        this.profileError.set(
          error.error?.message ?? 'Не удалось сохранить данные профиля'
        );
      }
    });
  }

  closeProfileSaved(): void {
    this.profileSaved.set(false);
  }

  updatePassword(): void {
    this.passwordError.set('');

    if (this.newPassword !== this.confirmPassword) {
      this.passwordError.set('Новые пароли не совпадают');
      return;
    }

    const user: UserPasswordUpdate = {
      currentPassword: this.currentPassword,
      newPassword: this.newPassword
    };

    this.userService.updatePassword(user).subscribe({
      next: () => {
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';
        this.passwordSaved.set(true);
      },
      error: error => {
        this.passwordError.set(
          error.error?.message ?? 'Не удалось изменить пароль'
        );
      }
    });
  }

  closePasswordSaved(): void {
    this.passwordSaved.set(false);
  }

  updateEmail(): void {
    this.emailError.set('');

    const currentEmail = this.userProfile()?.email;
    const newEmail = this.email.trim();

    if (currentEmail && newEmail.toLowerCase() === currentEmail.toLowerCase()) {
      this.emailError.set('Укажите новый email');
      return;
    }

    const user: UserEmailUpdate = {
      email: newEmail
    };

    this.emailLoading.set(true);

    this.userService.updateEmail(user).subscribe({
      next: () => {
        this.emailLoading.set(false);
        this.emailSaved.set(true);
      },
      error: error => {
        this.emailLoading.set(false);

        this.emailError.set(
          error.error?.message ?? 'Не удалось изменить email'
        );
      }
    });
  }

  finishEmailUpdate(): void {
    this.emailSaved.set(false);
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  deleteProfile(): void {
    this.deleteError.set('');

    const confirmed = confirm(
      'Вы действительно хотите удалить профиль? Активные бронирования будут отменены.'
    );

    if (!confirmed) {
      return;
    }

    this.userService.deleteProfile().subscribe({
      next: () => {
        this.authService.logout();
        this.router.navigate(['/']);
      },
      error: error => {
        this.deleteError.set(
          error.error?.message ?? 'Не удалось удалить профиль'
        );
      }
    });
  }
}
