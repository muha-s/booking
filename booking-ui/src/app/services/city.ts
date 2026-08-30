import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { City } from '../models/city';

@Injectable({
  providedIn: 'root'
})
export class CityService {

  private readonly apiUrl = 'http://localhost:8080/cities';

  constructor(private readonly http: HttpClient) {
  }

  findAll(): Observable<City[]> {
    return this.http.get<City[]>(this.apiUrl);
  }
}
