import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HotelAdmin } from '../../../models/hotel-admin/hotel-admin';
import { HotelAdminCreate } from '../../../models/hotel-admin/hotel-admin-create';
import { HotelShort } from '../../../models/hotel-admin/hotel-short';
import { HotelService } from '../../../services/hotel';
import { SuperAdminService } from '../../../services/super-admin/super-admin';

@Component({
  selector: 'app-hotel-admins',
  imports: [FormsModule],
  templateUrl: './hotel-admins.html',
  styleUrl: './hotel-admins.css'
})
export class HotelAdmins implements OnInit {

  hotelAdmins = signal<HotelAdmin[]>([]);
  hotels = signal<HotelShort[]>([]);

  showCreateForm = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  isCreating = signal(false);
  assigningAdminId = signal<number | null>(null);
  deletingAdminId = signal<number | null>(null);
  unassigningAssignment = signal('');

  hotelAdminToDelete = signal<HotelAdmin | null>(null);

  firstName = '';
  lastName = '';
  phone = '';
  email = '';

  selectedHotelIds: Record<number, number | null> = {};

  constructor(
    private readonly superAdminService: SuperAdminService,
    private readonly hotelService: HotelService
  ) {
  }

  ngOnInit(): void {
    this.loadHotelAdmins();
    this.loadHotels();
  }

  openCreateForm(): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.showCreateForm.set(true);
  }

  closeCreateForm(): void {
    this.showCreateForm.set(false);
    this.clearCreateForm();
  }

  createHotelAdmin(): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.isCreating.set(true);

    const hotelAdminCreate: HotelAdminCreate = {
      firstName: this.firstName,
      lastName: this.lastName,
      phone: this.phone,
      email: this.email
    };

    this.superAdminService.createHotelAdmin(hotelAdminCreate)
      .subscribe({
        next: () => {
          this.isCreating.set(false);

          this.successMessage.set(
            'Администратор создан. Письмо для активации аккаунта отправлено на email.'
          );

          this.showCreateForm.set(false);
          this.clearCreateForm();
          this.loadHotelAdmins();
        },

        error: error => {
          this.isCreating.set(false);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось создать администратора'
          );
        }
      });
  }

  assignHotel(hotelAdminId: number): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    const hotelId = this.selectedHotelIds[hotelAdminId];

    if (!hotelId) {
      this.errorMessage.set('Выберите отель');
      return;
    }

    this.assigningAdminId.set(hotelAdminId);

    this.superAdminService.assignHotelToAdmin(hotelAdminId, hotelId)
      .subscribe({
        next: updatedAdmin => {
          this.assigningAdminId.set(null);
          this.selectedHotelIds[hotelAdminId] = null;

          this.updateHotelAdmin(updatedAdmin);

          this.successMessage.set(
            'Отель назначен администратору'
          );
        },

        error: error => {
          this.assigningAdminId.set(null);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось назначить отель'
          );
        }
      });
  }

  unassignHotel(hotelAdminId: number, hotelId: number): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    const assignmentKey = `${hotelAdminId}-${hotelId}`;
    this.unassigningAssignment.set(assignmentKey);

    this.superAdminService.unassignHotelFromAdmin(hotelAdminId, hotelId)
      .subscribe({
        next: updatedAdmin => {
          this.unassigningAssignment.set('');

          this.updateHotelAdmin(updatedAdmin);

          this.successMessage.set(
            'Назначение отеля снято'
          );
        },

        error: error => {
          this.unassigningAssignment.set('');

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось снять назначение отеля'
          );
        }
      });
  }

  openDeleteModal(admin: HotelAdmin): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.hotelAdminToDelete.set(admin);
  }

  closeDeleteModal(): void {
    this.hotelAdminToDelete.set(null);
  }

  confirmDeleteHotelAdmin(): void {
    const admin = this.hotelAdminToDelete();

    if (!admin) {
      return;
    }

    this.deletingAdminId.set(admin.id);

    this.superAdminService.deleteHotelAdmin(admin.id)
      .subscribe({
        next: () => {
          this.deletingAdminId.set(null);
          this.hotelAdminToDelete.set(null);

          this.hotelAdmins.update(admins =>
            admins.filter(currentAdmin => currentAdmin.id !== admin.id)
          );

          delete this.selectedHotelIds[admin.id];

          this.successMessage.set(
            'Администратор удалён'
          );
        },

        error: error => {
          this.deletingAdminId.set(null);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось удалить администратора'
          );
        }
      });
  }

  getAvailableHotels(admin: HotelAdmin): HotelShort[] {
    return this.hotels().filter(
      hotel => !admin.managedHotels.some(
        managedHotel => managedHotel.id === hotel.id
      )
    );
  }

  private loadHotelAdmins(): void {
    this.superAdminService.findAllHotelAdmins()
      .subscribe({
        next: hotelAdmins => {
          this.hotelAdmins.set(hotelAdmins);
        },

        error: () => {
          this.errorMessage.set(
            'Не удалось загрузить администраторов отелей'
          );
        }
      });
  }

  private loadHotels(): void {
    this.hotelService.findAll()
      .subscribe({
        next: hotels => {
          this.hotels.set(hotels);
        },

        error: () => {
          this.errorMessage.set(
            'Не удалось загрузить список отелей'
          );
        }
      });
  }

  private updateHotelAdmin(updatedAdmin: HotelAdmin): void {
    this.hotelAdmins.update(admins =>
      admins.map(admin =>
        admin.id === updatedAdmin.id ? updatedAdmin : admin
      )
    );
  }

  private clearCreateForm(): void {
    this.firstName = '';
    this.lastName = '';
    this.phone = '';
    this.email = '';
  }
}
