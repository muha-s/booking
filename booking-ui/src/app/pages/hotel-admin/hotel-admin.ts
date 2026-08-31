import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { HotelManaged } from '../../models/hotel/hotel-managed';
import { AuthService } from '../../services/auth';
import { HotelAdminService } from '../../services/hotel-admin/hotel-admin';

@Component({
  selector: 'app-hotel-admin',
  imports: [RouterLink],
  templateUrl: './hotel-admin.html',
  styleUrl: './hotel-admin.css'
})
export class HotelAdmin implements OnInit {

  hotels = signal<HotelManaged[]>([]);
  errorMessage = signal('');

  constructor(
    private readonly hotelAdminService: HotelAdminService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.loadHotels();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  private loadHotels(): void {
    this.hotelAdminService.findManagedHotels()
      .subscribe({
        next: hotels => {
          this.hotels.set(hotels);
        },

        error: (error: HttpErrorResponse) => {
          if (
            error.status === 401
            || error.status === 403
            || error.status === 404
          ) {
            this.authService.logout();
            this.router.navigate(['/login']);
            return;
          }

          this.errorMessage.set(
            error.error?.message ?? 'Не удалось загрузить отели'
          );
        }
      });
  }
}
