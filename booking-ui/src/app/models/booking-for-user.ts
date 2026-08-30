export interface BookingForUser {
  id: number;

  room: {
    id: number;

    hotel: {
      id: number;
      name: string;

      city: {
        id: number;
        name: string;
      };

      address: string;
      numberOfStars: string;
      rating: number;
      basePricePerNight: number;
    };

    roomCapacity: string;
    roomType: string;
  };

  startDate: string;
  endDate: string;
  status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  totalPrice: number;
}
