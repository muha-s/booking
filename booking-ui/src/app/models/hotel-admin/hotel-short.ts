import { City } from '../city';

export interface HotelShort {
  id: number;
  name: string;
  city: City;
  address: string;
  numberOfStars: string;
  rating: number;
  basePricePerNight: number;
}
