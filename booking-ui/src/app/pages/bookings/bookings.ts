import { Component, HostListener, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BookingForUser } from '../../models/booking/booking-for-user';
import { BookingUpdate } from '../../models/booking/booking-update';
import { UserProfile } from '../../models/user/user-profile';
import { BookingService } from '../../services/booking';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-bookings',
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './bookings.html',
  styleUrl: './bookings.css'
})
export class Bookings implements OnInit {

  bookings = signal<BookingForUser[]>([]);
  userProfile = signal<UserProfile | null>(null);

  bookingForCancellation = signal<BookingForUser | null>(null);
  bookingForUpdate = signal<BookingForUser | null>(null);

  errorMessage = signal('');
  cancellationError = signal('');
  updateError = signal('');

  updateRoomCapacity = '';
  updateRoomType = '';
  updateStartDate = '';
  updateEndDate = '';

  private readonly pageSize = 10;

  private currentPage = 0;
  private lastPage = false;
  private loadingBookings = false;

  constructor(
    private readonly bookingService: BookingService,
    private readonly userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.loadBookings(true);
    this.loadUserProfile();
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

  private loadBookings(reset = false): void {
    if (this.loadingBookings) {
      return;
    }

    if (!reset && this.lastPage) {
      return;
    }

    if (reset) {
      this.currentPage = 0;
      this.lastPage = false;
      this.bookings.set([]);
    }

    this.loadingBookings = true;
    this.errorMessage.set('');

    this.bookingService.findMyBookings(
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: page => {
        if (reset) {
          this.bookings.set(page.content);
        } else {
          this.bookings.update(bookings => [
            ...bookings,
            ...page.content
          ]);
        }

        this.lastPage = page.last;

        if (!page.last) {
          this.currentPage++;
        }

        this.loadingBookings = false;
      },

      error: error => {
        this.loadingBookings = false;

        this.errorMessage.set(
          error.error?.message ?? 'Не удалось загрузить бронирования'
        );
      }
    });
  }

  private loadUserProfile(): void {
    this.userService.findProfile()
      .subscribe({
        next: profile => {
          this.userProfile.set(profile);
        }
      });
  }

  /* UPDATE */

  openUpdate(booking: BookingForUser): void {
    this.updateError.set('');

    this.updateRoomCapacity = booking.room.roomCapacity;
    this.updateRoomType = booking.room.roomType;
    this.updateStartDate = booking.startDate;
    this.updateEndDate = booking.endDate;

    this.bookingForUpdate.set(booking);
  }

  closeUpdate(): void {
    this.bookingForUpdate.set(null);
    this.updateError.set('');
  }

  confirmUpdate(): void {
    const booking = this.bookingForUpdate();

    if (booking === null) {
      return;
    }

    const update: BookingUpdate = {
      roomCapacity: this.updateRoomCapacity,
      roomType: this.updateRoomType,
      startDate: this.updateStartDate,
      endDate: this.updateEndDate
    };

    this.bookingService.updateMyBooking(
      booking.id,
      update
    ).subscribe({
      next: () => {
        this.bookingForUpdate.set(null);
        this.updateError.set('');

        this.loadBookings(true);
        this.loadUserProfile();
      },

      error: error => {
        this.updateError.set(
          error.error?.message ?? 'Не удалось изменить бронирование'
        );
      }
    });
  }

  /* CANCELLATION */

  openCancellation(booking: BookingForUser): void {
    this.cancellationError.set('');
    this.bookingForCancellation.set(booking);
  }

  closeCancellation(): void {
    this.bookingForCancellation.set(null);
    this.cancellationError.set('');
  }

  confirmCancellation(): void {
    const booking = this.bookingForCancellation();

    if (booking === null) {
      return;
    }

    this.bookingService.cancelMyBooking(booking.id)
      .subscribe({
        next: () => {
          this.bookingForCancellation.set(null);
          this.cancellationError.set('');

          this.loadBookings(true);
          this.loadUserProfile();
        },

        error: error => {
          this.cancellationError.set(
            error.error?.message ?? 'Не удалось отменить бронирование'
          );
        }
      });
  }

  /* DISPLAY */

  getStatusLabel(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'Активна';
      case 'COMPLETED':
        return 'Завершена';
      case 'CANCELLED':
        return 'Отменена';
      default:
        return status;
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'status-active';
      case 'COMPLETED':
        return 'status-completed';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return '';
    }
  }

  getRoomType(type: string): string {
    switch (type) {
      case 'STANDARD':
        return 'Standard';
      case 'COMFORT':
        return 'Comfort';
      case 'LUXURY':
        return 'Luxury';
      default:
        return type;
    }
  }

  getRoomCapacity(capacity: string): string {
    switch (capacity) {
      case 'ONE_SEAT':
        return '1 место';
      case 'TWO_SEAT':
        return '2 места';
      case 'THREE_SEAT':
        return '3 места';
      case 'FOUR_SEAT':
        return '4 места';
      default:
        return capacity;
    }
  }

  getStars(stars: string): string {
    switch (stars) {
      case 'ONE_STAR':
        return '★';
      case 'TWO_STARS':
        return '★★';
      case 'THREE_STARS':
        return '★★★';
      case 'THREE_STARS_PLUS':
        return '★★★+';
      case 'FOUR_STARS':
        return '★★★★';
      case 'FOUR_STARS_PLUS':
        return '★★★★+';
      case 'FIVE_STARS':
        return '★★★★★';
      case 'FIVE_STARS_PLUS':
        return '★★★★★+';
      default:
        return '';
    }
  }
}
