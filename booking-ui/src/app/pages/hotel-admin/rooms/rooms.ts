import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HotelManagedFull } from '../../../models/hotel/hotel-managed-full';
import { RoomManaged } from '../../../models/room/room-managed';
import { RoomManagedCreate } from '../../../models/room/room-managed-create';
import { HotelAdminService } from '../../../services/hotel-admin/hotel-admin';

@Component({
  selector: 'app-hotel-admin-rooms',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './rooms.html',
  styleUrl: './rooms.css'
})
export class HotelAdminRooms implements OnInit {

  hotel = signal<HotelManagedFull | null>(null);
  rooms = signal<RoomManaged[]>([]);
  errorMessage = signal('');
  roomToDelete = signal<RoomManaged | null>(null);
  deletingRoom = signal(false);

  showAddForm = signal(false);
  creatingRoom = signal(false);
  createErrorMessage = signal('');

  newRoomType = 'STANDARD';
  newRoomCapacity = 'ONE_SEAT';

  readonly roomTypes = [
    { value: 'STANDARD', label: 'Стандарт' },
    { value: 'COMFORT', label: 'Комфорт' },
    { value: 'LUXURY', label: 'Люкс' }
  ];

  readonly roomCapacities = [
    { value: 'ONE_SEAT', label: '1 место' },
    { value: 'TWO_SEAT', label: '2 места' },
    { value: 'THREE_SEAT', label: '3 места' },
    { value: 'FOUR_SEAT', label: '4 места' }
  ];

  private hotelId = 0;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly hotelAdminService: HotelAdminService
  ) {}

  ngOnInit(): void {
    const hotelId = Number(this.route.snapshot.paramMap.get('hotelId'));

    if (!hotelId) {
      this.router.navigate(['/hotel-admin']);
      return;
    }

    this.hotelId = hotelId;

    this.hotelAdminService.findManagedHotelById(hotelId).subscribe({
      next: hotel => {
        this.hotel.set(hotel);
      },

      error: () => {
        this.errorMessage.set('Не удалось загрузить данные отеля');
      }
    });

    this.hotelAdminService.findManagedRooms(hotelId).subscribe({
      next: rooms => {
        this.rooms.set(rooms);
      },

      error: () => {
        this.errorMessage.set('Не удалось загрузить комнаты');
      }
    });
  }

  openAddForm(): void {
    this.createErrorMessage.set('');
    this.newRoomType = 'STANDARD';
    this.newRoomCapacity = 'ONE_SEAT';
    this.showAddForm.set(true);
  }

  closeAddForm(): void {
    this.showAddForm.set(false);
    this.createErrorMessage.set('');
  }

  createRoom(): void {
    if (this.creatingRoom()) {
      return;
    }

    const room: RoomManagedCreate = {
      roomType: this.newRoomType,
      roomCapacity: this.newRoomCapacity
    };

    this.creatingRoom.set(true);
    this.createErrorMessage.set('');

    this.hotelAdminService.createManagedRoom(this.hotelId, room).subscribe({
      next: createdRoom => {
        this.rooms.update(rooms => [...rooms, createdRoom]);
        this.creatingRoom.set(false);
        this.showAddForm.set(false);
      },

      error: () => {
        this.creatingRoom.set(false);
        this.createErrorMessage.set('Не удалось добавить комнату');
      }
    });
  }

  formatRoomType(roomType: string): string {
    return this.roomTypes.find(type => type.value === roomType)?.label ?? roomType;
  }

  formatRoomCapacity(roomCapacity: string): string {
    return this.roomCapacities.find(
      capacity => capacity.value === roomCapacity
    )?.label ?? roomCapacity;
  }

  openDeleteModal(room: RoomManaged): void {
    this.roomToDelete.set(room);
  }

  closeDeleteModal(): void {
    if (this.deletingRoom()) {
      return;
    }

    this.roomToDelete.set(null);
  }

  confirmDeleteRoom(): void {
    const room = this.roomToDelete();

    if (!room || this.deletingRoom()) {
      return;
    }

    this.deletingRoom.set(true);

    this.hotelAdminService.deleteManagedRoom(
      this.hotelId,
      room.id
    ).subscribe({
      next: () => {
        this.rooms.update(rooms =>
          rooms.filter(existingRoom => existingRoom.id !== room.id)
        );

        this.deletingRoom.set(false);
        this.roomToDelete.set(null);
      },

      error: () => {
        this.deletingRoom.set(false);
        this.errorMessage.set('Не удалось удалить комнату');
      }
    });
  }
}
