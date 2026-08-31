import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HotelManaged } from '../../models/hotel/hotel-managed';
import { HotelAdminActivation } from '../../models/hotel-admin/hotel-admin-activation';
import { AuthService } from '../auth';
import { HotelManagedFull } from '../../models/hotel/hotel-managed-full';
import { RoomManaged } from '../../models/room/room-managed';
import { RoomManagedCreate } from '../../models/room/room-managed-create';
import { BookingManaged } from '../../models/booking/booking-managed';
import { HotelUpdate } from '../../models/hotel/hotel-update';

@Injectable({
  providedIn: 'root'
})
export class HotelAdminService {

  private readonly apiUrl = 'http://localhost:8080/hotel-admin';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  activate(hotelAdminActivation: HotelAdminActivation): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/activate`,
      hotelAdminActivation
    );
  }

  findManagedHotels(): Observable<HotelManaged[]> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<HotelManaged[]>(
      `${this.apiUrl}/hotels`,
      { headers }
    );
  }

  findManagedHotelById(hotelId: number): Observable<HotelManagedFull> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<HotelManagedFull>(
      `${this.apiUrl}/hotels/${hotelId}`,
      { headers }
    );
  }

  findManagedRooms(hotelId: number): Observable<RoomManaged[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<RoomManaged[]>(
      `${this.apiUrl}/hotels/${hotelId}/rooms`,
      { headers }
    );
  }

  createManagedRoom(
    hotelId: number,
    roomManagedCreate: RoomManagedCreate
  ): Observable<RoomManaged> {

    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<RoomManaged>(
      `${this.apiUrl}/hotels/${hotelId}/rooms`,
      roomManagedCreate,
      { headers }
    );
  }

  deleteManagedRoom(
    hotelId: number,
    roomId: number
  ): Observable<void> {

    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/hotels/${hotelId}/rooms/${roomId}`,
      { headers }
    );
  }

  findManagedBookings(hotelId: number): Observable<BookingManaged[]> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<BookingManaged[]>(
      `${this.apiUrl}/hotels/${hotelId}/bookings`,
      { headers }
    );
  }

  updateManagedHotel(
    hotelId: number,
    hotelUpdate: HotelUpdate
  ): Observable<HotelManagedFull> {

    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.put<HotelManagedFull>(
      `${this.apiUrl}/hotels/${hotelId}`,
      hotelUpdate,
      { headers }
    );
  }
}
