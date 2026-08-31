import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { City } from '../../../models/city';
import { HotelCreate } from '../../../models/hotel/hotel-create';
import { HotelShort } from '../../../models/hotel-admin/hotel-short';
import { CityService } from '../../../services/city';
import { HotelService } from '../../../services/hotel';

@Component({
  selector: 'app-super-admin-hotels',
  imports: [FormsModule],
  templateUrl: './hotels.html',
  styleUrl: './hotels.css'
})
export class SuperAdminHotels implements OnInit {

  hotels = signal<HotelShort[]>([]);
  cities = signal<City[]>([]);

  showCreateForm = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  isCreating = signal(false);
  deletingHotelId = signal<number | null>(null);

  hotelToDelete = signal<HotelShort | null>(null);

  name = '';
  cityId: number | null = null;
  address = '';
  numberOfStars: HotelCreate['numberOfStars'] | null = null;
  basePricePerNight: number | null = null;

  constructor(
    private readonly hotelService: HotelService,
    private readonly cityService: CityService
  ) {
  }

  ngOnInit(): void {
    this.loadHotels();
    this.loadCities();
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

  createHotel(): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (
      !this.name.trim()
      || !this.cityId
      || !this.address.trim()
      || !this.numberOfStars
      || !this.basePricePerNight
    ) {
      this.errorMessage.set('Заполните все поля');
      return;
    }

    const hotelCreate: HotelCreate = {
      name: this.name.trim(),
      cityId: this.cityId,
      address: this.address.trim(),
      numberOfStars: this.numberOfStars,
      basePricePerNight: this.basePricePerNight
    };

    this.isCreating.set(true);

    this.hotelService.create(hotelCreate)
      .subscribe({
        next: hotel => {
          this.isCreating.set(false);

          this.hotels.update(hotels => [...hotels, hotel]);

          this.showCreateForm.set(false);
          this.clearCreateForm();

          this.successMessage.set('Отель создан');
        },

        error: error => {
          this.isCreating.set(false);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось создать отель'
          );
        }
      });
  }

  openDeleteModal(hotel: HotelShort): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.hotelToDelete.set(hotel);
  }

  closeDeleteModal(): void {
    this.hotelToDelete.set(null);
  }

  confirmDeleteHotel(): void {
    const hotel = this.hotelToDelete();

    if (!hotel) {
      return;
    }

    this.deletingHotelId.set(hotel.id);

    this.hotelService.deleteById(hotel.id)
      .subscribe({
        next: () => {
          this.deletingHotelId.set(null);
          this.hotelToDelete.set(null);

          this.hotels.update(hotels =>
            hotels.filter(currentHotel => currentHotel.id !== hotel.id)
          );

          this.successMessage.set('Отель удалён');
        },

        error: error => {
          this.deletingHotelId.set(null);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось удалить отель'
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
          this.errorMessage.set('Не удалось загрузить отели');
        }
      });
  }

  private loadCities(): void {
    this.cityService.findAll()
      .subscribe({
        next: cities => {
          this.cities.set(cities);
        },

        error: () => {
          this.errorMessage.set('Не удалось загрузить города');
        }
      });
  }

  private clearCreateForm(): void {
    this.name = '';
    this.cityId = null;
    this.address = '';
    this.numberOfStars = null;
    this.basePricePerNight = null;
  }
}
