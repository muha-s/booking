import { HotelShort } from './hotel-short';

export interface HotelAdmin {
  id: number;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  emailVerified: boolean;
  managedHotels: HotelShort[];
}
