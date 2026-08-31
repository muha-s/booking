export interface HotelCreate {
  name: string;
  cityId: number;
  address: string;
  numberOfStars:
    | 'ONE_STAR'
    | 'TWO_STARS'
    | 'THREE_STARS'
    | 'THREE_STARS_PLUS'
    | 'FOUR_STARS'
    | 'FOUR_STARS_PLUS'
    | 'FIVE_STARS'
    | 'FIVE_STARS_PLUS';
  basePricePerNight: number;
}
