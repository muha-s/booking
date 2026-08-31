import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { City } from '../../../models/city';
import { CityService } from '../../../services/city';

@Component({
  selector: 'app-super-admin-cities',
  imports: [FormsModule],
  templateUrl: './cities.html',
  styleUrl: './cities.css'
})
export class SuperAdminCities implements OnInit {

  cities = signal<City[]>([]);

  showCreateForm = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  isCreating = signal(false);
  deletingCityId = signal<number | null>(null);

  cityToDelete = signal<City | null>(null);

  cityName = '';

  constructor(
    private readonly cityService: CityService
  ) {
  }

  ngOnInit(): void {
    this.loadCities();
  }

  openCreateForm(): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.showCreateForm.set(true);
  }

  closeCreateForm(): void {
    this.showCreateForm.set(false);
    this.cityName = '';
  }

  createCity(): void {
    this.errorMessage.set('');
    this.successMessage.set('');

    const name = this.cityName.trim();

    if (!name) {
      this.errorMessage.set('Введите название города');
      return;
    }

    this.isCreating.set(true);

    this.cityService.create(name)
      .subscribe({
        next: city => {
          this.isCreating.set(false);
          this.cities.update(cities => [...cities, city]);

          this.cityName = '';
          this.showCreateForm.set(false);

          this.successMessage.set('Город создан');
        },

        error: error => {
          this.isCreating.set(false);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось создать город'
          );
        }
      });
  }

  openDeleteModal(city: City): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.cityToDelete.set(city);
  }

  closeDeleteModal(): void {
    this.cityToDelete.set(null);
  }

  confirmDeleteCity(): void {
    const city = this.cityToDelete();

    if (!city) {
      return;
    }

    this.deletingCityId.set(city.id);

    this.cityService.deleteById(city.id)
      .subscribe({
        next: () => {
          this.deletingCityId.set(null);
          this.cityToDelete.set(null);

          this.cities.update(cities =>
            cities.filter(currentCity => currentCity.id !== city.id)
          );

          this.successMessage.set('Город удалён');
        },

        error: error => {
          this.deletingCityId.set(null);

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось удалить город'
          );
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
}
