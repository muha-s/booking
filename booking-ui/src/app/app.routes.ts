import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { VerifyEmail } from './pages/verify-email/verify-email';
import { Bookings } from './pages/bookings/bookings';
import { HotelReviews } from './pages/hotel-reviews/hotel-reviews';
import { Settings } from './pages/settings/settings';
import { Review } from './pages/review/review';
import { SuperAdmin } from './pages/super-admin/super-admin';
import { superAdminGuard } from './guards/super-admin.guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'verify-email', component: VerifyEmail },
  { path: 'bookings', component: Bookings },
  { path: 'settings', component: Settings },
  { path: 'review/:bookingId', component: Review },
  { path: 'hotels/:hotelId/reviews', component: HotelReviews },
  { path: 'super-admin', component: SuperAdmin, canActivate: [superAdminGuard] }
];
