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
import { HotelAdmins } from './pages/super-admin/hotel-admins/hotel-admins';
import { HotelAdminActivate } from './pages/hotel-admin/activate/hotel-admin-activate';
import { SuperAdminCities } from './pages/super-admin/cities/cities';
import { SuperAdminHotels } from './pages/super-admin/hotels/hotels';
import { SuperAdminUsers } from './pages/super-admin/users/users';
import { HotelAdmin } from './pages/hotel-admin/hotel-admin';
import { hotelAdminGuard } from './guards/hotel-admin.guard';
import { HotelDetails } from './pages/hotel-admin/hotel-details/hotel-details';
import { HotelAdminRooms } from './pages/hotel-admin/rooms/rooms';
import { HotelAdminBookings } from './pages/hotel-admin/bookings/bookings';
import { RestoreAccount } from './pages/restore-account/restore-account';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'verify-email', component: VerifyEmail },
  { path: 'restore-account', component: RestoreAccount },
  { path: 'bookings', component: Bookings },
  { path: 'settings', component: Settings },
  { path: 'review/:bookingId', component: Review },
  { path: 'hotels/:hotelId/reviews', component: HotelReviews },
  { path: 'super-admin', component: SuperAdmin, canActivate: [superAdminGuard] },
  { path: 'super-admin/hotel-admins', component: HotelAdmins, canActivate: [superAdminGuard] },
  { path: 'hotel-admin/activate', component: HotelAdminActivate },
  { path: 'hotel-admin', component: HotelAdmin, canActivate: [hotelAdminGuard] },
  { path: 'super-admin/cities', component: SuperAdminCities, canActivate: [superAdminGuard] },
  { path: 'super-admin/hotels', component: SuperAdminHotels, canActivate: [superAdminGuard] },
  { path: 'super-admin/users', component: SuperAdminUsers, canActivate: [superAdminGuard] },
  { path: 'hotel-admin/hotels/:hotelId',component: HotelDetails,canActivate: [hotelAdminGuard] },
  { path: 'hotel-admin/hotels/:hotelId/rooms', component: HotelAdminRooms, canActivate: [hotelAdminGuard] },
  { path: 'hotel-admin/hotels/:hotelId/bookings', component: HotelAdminBookings, canActivate: [hotelAdminGuard] },

];
