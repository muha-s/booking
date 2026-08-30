import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RoomAvailable } from '../models/room-available';
import { RoomSearchCriteria } from '../models/room-search-criteria';

@Injectable({
  providedIn: 'root'
})
export class RoomSearchService {

  private readonly apiUrl = 'http://localhost:8080/rooms/available';

  constructor(private readonly http: HttpClient) {
  }

 findAvailable(criteria: RoomSearchCriteria): Observable<RoomAvailable[]> {

   return this.http.get<RoomAvailable[]>(this.apiUrl, {
     params: {
       cityId: criteria.cityId,
       startDate: criteria.startDate,
       endDate: criteria.endDate,
       roomCapacity: criteria.roomCapacity ?? '',
       roomType: criteria.roomType ?? ''
     }
   });
 }
}

