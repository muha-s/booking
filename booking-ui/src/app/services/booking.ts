import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BookingCreate } from '../models/booking-create';
import { BookingForUser } from '../models/booking-for-user';
import { BookingForReview } from '../models/booking-for-review';
import { BookingUpdate } from '../models/booking-update';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private readonly apiUrl = 'http://localhost:8080/bookings';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  create(booking: BookingCreate): Observable<unknown> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post(
      this.apiUrl,
      booking,
      { headers }
    );
  }

  findMyBookings(): Observable<BookingForUser[]> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<BookingForUser[]>(
      `${this.apiUrl}/my`,
      { headers }
    );
  }

  findMyBookingForReview(id: number): Observable<BookingForReview> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<BookingForReview>(
      `${this.apiUrl}/my/${id}/review`,
      { headers }
    );
  }

  updateMyBooking(
    id: number,
    booking: BookingUpdate
  ): Observable<unknown> {

    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.put(
      `${this.apiUrl}/my/${id}`,
      booking,
      { headers }
    );
  }

  cancelMyBooking(id: number): Observable<void> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/my/${id}`,
      { headers }
    );
  }
}
