import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HotelManagedFull } from '../../../models/hotel/hotel-managed-full';
import { HotelUpdate } from '../../../models/hotel/hotel-update';
import { HotelAdminService } from '../../../services/hotel-admin/hotel-admin';

@Component({
  selector: 'app-hotel-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './hotel-details.html',
  styleUrl: './hotel-details.css'
})
export class HotelDetails implements OnInit {

  hotel = signal<HotelManagedFull | null>(null);
  errorMessage = signal('');

  editMode = signal(false);
  updatingHotel = signal(false);
  updateErrorMessage = signal('');

  editName = '';
  editAddress = '';
  editNumberOfStars = '';
  editBasePricePerNight = 0;

  readonly starOptions = [
    { value: 'ONE_STAR', label: '★' },
    { value: 'TWO_STARS', label: '★★' },
    { value: 'THREE_STARS', label: '★★★' },
    { value: 'THREE_STARS_PLUS', label: '★★★+' },
    { value: 'FOUR_STARS', label: '★★★★' },
    { value: 'FOUR_STARS_PLUS', label: '★★★★+' },
    { value: 'FIVE_STARS', label: '★★★★★' },
    { value: 'FIVE_STARS_PLUS', label: '★★★★★+' }
  ];

  private hotelId = 0;

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

    this.hotelId = hotelId;

    this.hotelAdminService.findManagedHotelById(hotelId).subscribe({
      next: hotel => {
        this.hotel.set(hotel);
      },

      error: () => {
        this.errorMessage.set('Не удалось загрузить данные отеля');
      }
    });
  }

  openEditForm(): void {
    const hotel = this.hotel();

    if (!hotel) {
      return;
    }

    this.editName = hotel.name;
    this.editAddress = hotel.address;
    this.editNumberOfStars = hotel.numberOfStars;
    this.editBasePricePerNight = hotel.basePricePerNight;

    this.updateErrorMessage.set('');
    this.editMode.set(true);
  }

  closeEditForm(): void {
    if (this.updatingHotel()) {
      return;
    }

    this.updateErrorMessage.set('');
    this.editMode.set(false);
  }

  updateHotel(): void {
    if (this.updatingHotel()) {
      return;
    }

    const hotelUpdate: HotelUpdate = {
      name: this.editName.trim(),
      address: this.editAddress.trim(),
      numberOfStars: this.editNumberOfStars,
      basePricePerNight: this.editBasePricePerNight
    };

    this.updatingHotel.set(true);
    this.updateErrorMessage.set('');

    this.hotelAdminService.updateManagedHotel(
      this.hotelId,
      hotelUpdate
    ).subscribe({
      next: updatedHotel => {
        this.hotel.set(updatedHotel);
        this.updatingHotel.set(false);
        this.editMode.set(false);
      },

      error: () => {
        this.updatingHotel.set(false);
        this.updateErrorMessage.set('Не удалось обновить данные отеля');
      }
    });
  }

  formatStars(numberOfStars: string): string {
    const stars: Record<string, string> = {
      ONE_STAR: '★',
      TWO_STARS: '★★',
      THREE_STARS: '★★★',
      THREE_STARS_PLUS: '★★★+',
      FOUR_STARS: '★★★★',
      FOUR_STARS_PLUS: '★★★★+',
      FIVE_STARS: '★★★★★',
      FIVE_STARS_PLUS: '★★★★★+'
    };

    return stars[numberOfStars] ?? numberOfStars;
  }
}
