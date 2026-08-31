import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserCreate } from '../models/user/user-create';
import { UserEmailUpdate } from '../models/user/user-email-update';
import { UserPasswordUpdate } from '../models/user/user-password-update';
import { UserProfile } from '../models/user/user-profile';
import { UserRestore } from '../models/user/user-restore';
import { UserRestoreRequest } from '../models/user/user-restore-request';
import { UserUpdate } from '../models/user/user-update';
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

  requestRestore(userRestoreRequest: UserRestoreRequest): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/restore-request`,
      userRestoreRequest
    );
  }

  restore(userRestore: UserRestore): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/restore`,
      userRestore
    );
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
