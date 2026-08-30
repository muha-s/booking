import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/user-profile';
import { UserCreate } from '../models/user-create';
import { UserUpdate } from '../models/user-update';
import { UserPasswordUpdate } from '../models/user-password-update';
import { UserEmailUpdate } from '../models/user-email-update';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8080/users';

  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {}

  findProfile(): Observable<UserProfile> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.get<UserProfile>(`${this.apiUrl}/me`, { headers });
  }

  create(user: UserCreate): Observable<unknown> {
    return this.http.post(this.apiUrl, user);
  }

  updateProfile(user: UserUpdate): Observable<UserProfile> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.put<UserProfile>(`${this.apiUrl}/me`, user, { headers });
  }

  updatePassword(user: UserPasswordUpdate): Observable<void> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.put<void>(`${this.apiUrl}/me/password`, user, { headers });
  }

  updateEmail(user: UserEmailUpdate): Observable<void> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.put<void>(`${this.apiUrl}/me/email`, user, { headers });
  }

  deleteProfile(): Observable<void> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.delete<void>(`${this.apiUrl}/me`, { headers });
  }

  verifyEmail(token: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/verify-email`, null, {
      params: { token }
    });
  }
}
