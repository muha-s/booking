import { Component, OnInit, signal } from '@angular/core';
import { City } from '../../models/city';
import { CityService } from '../../services/city';
import { FormsModule } from '@angular/forms';
import { RoomSearchCriteria } from '../../models/room-search-criteria';
import { RoomAvailable } from '../../models/room-available';
import { RoomSearchService } from '../../services/room-search';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';
import { UserService } from '../../services/user';
import { UserProfile } from '../../models/user/user-profile';
import { BookingService } from '../../services/booking';
import { BookingCreate } from '../../models/booking/booking-create';
import { BookingConfirmation } from '../../models/booking/booking-confirmation';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  imports: [FormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  cities = signal<City[]>([]);

  selectedCityId: number | null = null;
  checkIn = '';
  checkOut = '';
  selectedCapacity = '';
  selectedRoomType = '';

  rooms = signal<RoomAvailable[]>([]);
  searchPerformed = signal(false);

  userProfile = signal<UserProfile | null>(null);

  selectedRoomForBooking = signal<RoomAvailable | null>(null);
  bookingConfirmation = signal<BookingConfirmation | null>(null);

  searchError = signal('');

  constructor(
    private readonly cityService: CityService,
    private readonly roomSearchService: RoomSearchService,
    private readonly authService: AuthService,
    private readonly userService: UserService,
    private readonly bookingService: BookingService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {

    this.cityService.findAll()
      .subscribe(cities => {
        this.cities.set(cities);
      });

    if (this.authService.isLoggedIn()) {

      this.userService.findProfile()
        .subscribe({
          next: profile => {
            this.userProfile.set(profile);
          },

          error: () => {
            this.authService.logout();
            this.userProfile.set(null);
          }
        });
    }
    this.restoreSearchFromUrl();
  }

  search(): void {

    this.searchError.set('');

    if (this.selectedCityId === null) {
      this.searchError.set('Выберите город');
      return;
    }

    if (!this.checkIn) {
      this.searchError.set('Укажите дату заезда');
      return;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const checkInDate = new Date(
      this.checkIn + 'T00:00:00'
    );

    if (checkInDate < today) {
      this.searchError.set(
        'Дата заезда не может быть в прошлом'
      );
      return;
    }

    if (!this.checkOut) {
      this.searchError.set('Укажите дату выезда');
      return;
    }

    const checkOutDate = new Date(
      this.checkOut + 'T00:00:00'
    );

    if (checkOutDate <= checkInDate) {
      this.searchError.set(
        'Дата выезда должна быть позже даты заезда'
      );
      return;
    }

    const criteria: RoomSearchCriteria = {
      cityId: this.selectedCityId,
      startDate: this.checkIn,
      endDate: this.checkOut,
      roomCapacity: this.selectedCapacity || undefined,
      roomType: this.selectedRoomType || undefined
    };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        cityId: this.selectedCityId,
        checkIn: this.checkIn,
        checkOut: this.checkOut,
        capacity: this.selectedCapacity || null,
        roomType: this.selectedRoomType || null
      }
    });

    this.loadRooms(criteria);
  }

  private restoreSearchFromUrl(): void {

    const cityId =
      this.route.snapshot.queryParamMap.get('cityId');

    const checkIn =
      this.route.snapshot.queryParamMap.get('checkIn');

    const checkOut =
      this.route.snapshot.queryParamMap.get('checkOut');

    const capacity =
      this.route.snapshot.queryParamMap.get('capacity');

    const roomType =
      this.route.snapshot.queryParamMap.get('roomType');

    if (!cityId || !checkIn || !checkOut) {
      return;
    }

    this.selectedCityId = Number(cityId);
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.selectedCapacity = capacity ?? '';
    this.selectedRoomType = roomType ?? '';

    const criteria: RoomSearchCriteria = {
      cityId: this.selectedCityId,
      startDate: this.checkIn,
      endDate: this.checkOut,
      roomCapacity: this.selectedCapacity || undefined,
      roomType: this.selectedRoomType || undefined
    };

    this.loadRooms(criteria);
  }

  private loadRooms(criteria: RoomSearchCriteria): void {

    this.roomSearchService.findAvailable(criteria)
      .subscribe({
        next: rooms => {
          this.rooms.set(rooms);
          this.searchPerformed.set(true);
        },

        error: (error: HttpErrorResponse) => {

          this.rooms.set([]);
          this.searchPerformed.set(false);

          this.searchError.set(
            error.error?.message ?? 'Ошибка поиска номеров'
          );
        }
      });
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

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {

    this.authService.logout();

    this.userProfile.set(null);
    this.selectedRoomForBooking.set(null);
    this.bookingConfirmation.set(null);

    this.resetSearch();
  }

  selectRoom(room: RoomAvailable): void {

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    if (this.userProfile()?.role !== 'USER') {
      return;
    }

    this.selectedRoomForBooking.set(room);
  }

  closeBookingSelection(): void {
    this.selectedRoomForBooking.set(null);
  }

  confirmBooking(): void {

    const room = this.selectedRoomForBooking();

    if (room === null) {
      return;
    }

    const booking: BookingCreate = {
      hotelId: room.hotelId,
      roomCapacity: room.roomCapacity,
      roomType: room.roomType,
      startDate: this.checkIn,
      endDate: this.checkOut
    };

    this.bookingService.create(booking)
      .subscribe({

        next: () => {

          this.selectedRoomForBooking.set(null);

          this.bookingConfirmation.set({
            hotelName: room.hotelName,
            hotelAddress: room.hotelAddress,
            hotelStars: room.hotelStars,
            roomType: room.roomType,
            roomCapacity: room.roomCapacity,
            startDate: this.checkIn,
            endDate: this.checkOut,
            totalStayPrice: room.totalStayPrice
          });

          this.userService.findProfile()
            .subscribe(profile => {
              this.userProfile.set(profile);
            });

          this.selectedCityId = null;
          this.checkIn = '';
          this.checkOut = '';
          this.selectedCapacity = '';
          this.selectedRoomType = '';

          this.rooms.set([]);
          this.searchPerformed.set(false);

          this.router.navigate(['/']);
        },

        error: (error: HttpErrorResponse) => {

          this.searchError.set(
            error.error?.message
              ?? 'Не удалось создать бронирование'
          );
        }
      });
  }

  closeBookingConfirmation(): void {
    this.bookingConfirmation.set(null);
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

  closeSearchError(): void {
    this.searchError.set('');
  }

  resetSearch(): void {

    this.selectedCityId = null;
    this.checkIn = '';
    this.checkOut = '';
    this.selectedCapacity = '';
    this.selectedRoomType = '';

    this.rooms.set([]);
    this.searchPerformed.set(false);
    this.searchError.set('');
    this.selectedRoomForBooking.set(null);

    this.router.navigate(['/']);
  }
}
