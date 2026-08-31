import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserSummary } from '../../../models/user/user-summary';
import { SuperAdminService } from '../../../services/super-admin/super-admin';

@Component({
  selector: 'app-super-admin-users',
  imports: [RouterLink],
  templateUrl: './users.html',
  styleUrl: './users.css'
})
export class SuperAdminUsers implements OnInit {

  users = signal<UserSummary[]>([]);
  errorMessage = signal('');
  successMessage = signal('');

  userToDelete = signal<UserSummary | null>(null);
  deletingUserId = signal<number | null>(null);

  constructor(
    private readonly superAdminService: SuperAdminService
  ) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  openDeleteModal(user: UserSummary): void {
    if (user.deletedAt !== null) {
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');
    this.userToDelete.set(user);
  }

  closeDeleteModal(): void {
    this.userToDelete.set(null);
  }

  confirmDeleteUser(): void {
    const user = this.userToDelete();

    if (!user) {
      return;
    }

    this.deletingUserId.set(user.id);

    this.superAdminService.deleteUser(user.id)
      .subscribe({
        next: () => {
          this.deletingUserId.set(null);
          this.userToDelete.set(null);

          const deletedAt = new Date().toISOString();

          this.users.update(users =>
            users.map(currentUser =>
              currentUser.id === user.id
                ? { ...currentUser, deletedAt }
                : currentUser
            )
          );

          this.successMessage.set('Пользователь удалён');
        },

        error: error => {
          this.deletingUserId.set(null);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось удалить пользователя'
          );
        }
      });
  }

  private loadUsers(): void {
    this.superAdminService.findAllUsers()
      .subscribe({
        next: users => {
          this.users.set(users);
        },

        error: () => {
          this.errorMessage.set('Не удалось загрузить пользователей');
        }
      });
  }
}
