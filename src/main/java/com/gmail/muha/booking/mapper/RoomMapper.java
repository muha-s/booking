package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.room_dto.RoomCreateDto;
import com.gmail.muha.booking.dto.room_dto.RoomResponseDto;
import com.gmail.muha.booking.dto.room_dto.RoomUpdateDto;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.entity.Room;

public class RoomMapper {

    private RoomMapper() {
    }

    public Room toEntity(RoomCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new Room(dto.getHotelId(), dto.getRoomType(), dto.getPrice());
    }

    public RoomResponseDto toResponseDto(Room room, Hotel hotel) {
        if (room == null) {
            return null;
        }
        RoomResponseDto dto = new RoomResponseDto();

        dto.setId(room.getId());
        dto.setHotelName(hotel.getName());
        dto.setHotelId(hotel.getId());
        dto.setRoomType(room.getRoomType());
        dto.setPrice(room.getPrice());
        return dto;
    }

    public RoomUpdateDto toUpdateDto(Room room) {
        if (room == null) {
            return null;
        }
        return new RoomUpdateDto(
                room.getId(), room.getRoomType(), room.getPrice());
    }

    private static class Holder {
        private static final RoomMapper INSTANCE = new RoomMapper();
    }

    public static RoomMapper getInstance() {
        return RoomMapper.Holder.INSTANCE;
    }
}
