import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HotelCreate } from '../models/hotel/hotel-create';
import { HotelShort } from '../models/hotel-admin/hotel-short';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  private readonly apiUrl = 'http://localhost:8080/hotels';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  findAll(): Observable<HotelShort[]> {
    return this.http.get<HotelShort[]>(this.apiUrl);
  }

  create(hotelCreate: HotelCreate): Observable<HotelShort> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<HotelShort>(
      this.apiUrl,
      hotelCreate,
      { headers }
    );
  }

  deleteById(hotelId: number): Observable<void> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/${hotelId}`,
      { headers }
    );
  }
}
