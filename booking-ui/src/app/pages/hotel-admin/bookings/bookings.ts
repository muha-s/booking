import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit, signal } from '@angular/core';
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

  private readonly pageSize = 10;

  private hotelId = 0;
  private currentPage = 0;
  private lastPage = false;
  private loadingBookings = false;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly hotelAdminService: HotelAdminService
  ) {
  }

  ngOnInit(): void {
    this.hotelId = Number(
      this.route.snapshot.paramMap.get('hotelId')
    );

    if (!this.hotelId) {
      this.router.navigate(['/hotel-admin']);
      return;
    }

    this.hotelAdminService.findManagedHotelById(this.hotelId)
      .subscribe({
        next: hotel => {
          this.hotel.set(hotel);
        },

        error: () => {
          this.errorMessage.set(
            'Не удалось загрузить данные отеля'
          );
        }
      });

    this.loadBookings();
  }

  @HostListener('window:scroll')
  onWindowScroll(): void {
    const scrollPosition = window.innerHeight + window.scrollY;
    const pageHeight = document.documentElement.scrollHeight;
    const loadThreshold = 300;

    if (scrollPosition >= pageHeight - loadThreshold) {
      this.loadBookings();
    }
  }

  private loadBookings(): void {
    if (this.loadingBookings || this.lastPage) {
      return;
    }

    this.loadingBookings = true;
    this.errorMessage.set('');

    this.hotelAdminService.findManagedBookings(
      this.hotelId,
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: page => {
        this.bookings.update(bookings => [
          ...bookings,
          ...page.content
        ]);

        this.lastPage = page.last;

        if (!page.last) {
          this.currentPage++;
        }

        this.loadingBookings = false;
      },

      error: () => {
        this.loadingBookings = false;
        this.errorMessage.set(
          'Не удалось загрузить бронирования'
        );
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
