import { City } from '../city';

export interface HotelManagedFull {
  id: number;
  name: string;
  city: City;
  address: string;
  numberOfStars: string;
  rating: number;
  basePricePerNight: number;
  balance: number;
}
