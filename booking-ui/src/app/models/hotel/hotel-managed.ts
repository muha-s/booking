import { City } from '../city';

export interface HotelManaged {
  id: number;
  name: string;
  city: City;
  address: string;
}
