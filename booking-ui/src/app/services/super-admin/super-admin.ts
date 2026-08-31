import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HotelAdmin } from '../../models/hotel-admin/hotel-admin';
import { HotelAdminCreate } from '../../models/hotel-admin/hotel-admin-create';
import { AuthService } from '../auth';
import { UserSummary } from '../../models/user/user-summary';

@Injectable({
  providedIn: 'root'
})
export class SuperAdminService {

  private readonly apiUrl = 'http://localhost:8080/super-admin';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  findAllHotelAdmins(): Observable<HotelAdmin[]> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<HotelAdmin[]>(
      `${this.apiUrl}/hotel-admins`,
      { headers }
    );
  }

  createHotelAdmin(
    hotelAdminCreate: HotelAdminCreate
  ): Observable<HotelAdmin> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<HotelAdmin>(
      `${this.apiUrl}/hotel-admins`,
      hotelAdminCreate,
      { headers }
    );
  }

  assignHotelToAdmin(
    hotelAdminId: number,
    hotelId: number
  ): Observable<HotelAdmin> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.put<HotelAdmin>(
      `${this.apiUrl}/hotel-admins/${hotelAdminId}/hotels/${hotelId}`,
      null,
      { headers }
    );
  }

  unassignHotelFromAdmin(
    hotelAdminId: number,
    hotelId: number
  ): Observable<HotelAdmin> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<HotelAdmin>(
      `${this.apiUrl}/hotel-admins/${hotelAdminId}/hotels/${hotelId}`,
      { headers }
    );
  }

  deleteHotelAdmin(hotelAdminId: number): Observable<void> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/hotel-admins/${hotelAdminId}`,
      { headers }
    );
  }

  findAllUsers(): Observable<UserSummary[]> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<UserSummary[]>(
      `${this.apiUrl}/users`,
      { headers }
    );
  }

  deleteUser(userId: number): Observable<void> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/users/${userId}`,
      { headers }
    );
  }
}
