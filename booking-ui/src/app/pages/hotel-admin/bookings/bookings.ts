import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BookingManaged } from '../../../models/booking/booking-managed';
import { HotelManagedFull } from '../../../models/hotel/hotel-managed-full';
import { HotelAdminService } from '../../../services/hotel-admin/hotel-admin';

@Component({
  selector: 'app-hotel-admin-bookings',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './bookings.html',
  styleUrl: './bookings.css'
})
export class HotelAdminBookings implements OnInit {

  hotel = signal<HotelManagedFull | null>(null);
  bookings = signal<BookingManaged[]>([]);
  errorMessage = signal('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly hotelAdminService: HotelAdminService
  ) {}

  ngOnInit(): void {
    const hotelId = Number(this.route.snapshot.paramMap.get('hotelId'));

    if (!hotelId) {
      this.router.navigate(['/hotel-admin']);
      return;
    }

    this.hotelAdminService.findManagedHotelById(hotelId).subscribe({
      next: hotel => {
        this.hotel.set(hotel);
      },

      error: () => {
        this.errorMessage.set('Не удалось загрузить данные отеля');
      }
    });

    this.hotelAdminService.findManagedBookings(hotelId).subscribe({
      next: bookings => {
        this.bookings.set(bookings);
      },

      error: () => {
        this.errorMessage.set('Не удалось загрузить бронирования');
      }
    });
  }

  formatRoomType(roomType: string): string {
    const roomTypes: Record<string, string> = {
      STANDARD: 'Стандарт',
      COMFORT: 'Комфорт',
      LUXURY: 'Люкс'
    };

    return roomTypes[roomType] ?? roomType;
  }

  formatRoomCapacity(roomCapacity: string): string {
    const capacities: Record<string, string> = {
      ONE_SEAT: '1 место',
      TWO_SEAT: '2 места',
      THREE_SEAT: '3 места',
      FOUR_SEAT: '4 места'
    };

    return capacities[roomCapacity] ?? roomCapacity;
  }

  formatStatus(status: string): string {
    const statuses: Record<string, string> = {
      ACTIVE: 'Активно',
      COMPLETED: 'Завершено',
      CANCELLED: 'Отменено'
    };

    return statuses[status] ?? status;
  }
}
