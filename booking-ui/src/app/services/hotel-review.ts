import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth';
import { HotelReviewCreate } from '../models/hotel-review-create';
import { HotelReviewResponse } from '../models/hotel-review-response';
import { HotelReviewForHotel } from '../models/hotel-review-for-hotel';

@Injectable({
  providedIn: 'root'
})
export class HotelReviewService {

  private readonly apiUrl = 'http://localhost:8080/hotel-reviews';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {
  }

  create(review: HotelReviewCreate): Observable<HotelReviewResponse> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<HotelReviewResponse>(
      this.apiUrl,
      review,
      { headers }
    );
  }

  findCommentsByHotelId(hotelId: number): Observable<HotelReviewForHotel[]> {
    const token = this.authService.getToken();

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.get<HotelReviewForHotel[]>(
      `${this.apiUrl}/hotel/${hotelId}`,
      { headers }
    );
  }
}
