import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { City } from '../models/city';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class CityService {

  private readonly apiUrl = 'http://localhost:8080/cities';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  findAll(): Observable<City[]> {
    return this.http.get<City[]>(this.apiUrl);
  }

  create(name: string): Observable<City> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<City>(
      this.apiUrl,
      { name },
      { headers }
    );
  }

  deleteById(cityId: number): Observable<void> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.delete<void>(
      `${this.apiUrl}/${cityId}`,
      { headers }
    );
  }
}
