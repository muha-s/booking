export interface HotelReviewCreate {
  bookingId: number;
  score: number | null;
  comment: string | null;
}
