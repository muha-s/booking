export interface BookingManaged {
  userFirstName: string;
  userLastName: string;
  userPhone: string;
  userEmail: string;

  roomType: string;
  roomCapacity: string;

  startDate: string;
  endDate: string;
  totalPrice: number;
  status: string;
}
