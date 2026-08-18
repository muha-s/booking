package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.room.*;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {


    private final ShortDtoMapper shortDtoMapper;

    public RoomMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public RoomFullDto toFullDto(Room room) {

        RoomFullDto roomFullDto = new RoomFullDto();

        roomFullDto.setId(room.getId());
        roomFullDto.setHotel(shortDtoMapper.toHotelShortDto(room.getHotel()));
        roomFullDto.setRoomCapacity(room.getRoomCapacity());
        roomFullDto.setRoomType(room.getRoomType());
        roomFullDto.setBookings(shortDtoMapper.toBookingShortDtoList(room.getBookings()));

        return roomFullDto;
    }

    public RoomShortDto toShortDto(Room room) {
        RoomShortDto roomShortDto = new RoomShortDto();

        roomShortDto.setId(room.getId());
        roomShortDto.setHotel(shortDtoMapper.toHotelShortDto(room.getHotel()));
        roomShortDto.setRoomCapacity(room.getRoomCapacity());
        roomShortDto.setRoomType(room.getRoomType());
        return roomShortDto;
    }

    public Room toEntity(RoomCreateDto roomCreateDto, Hotel roomHotel) {
        Room room = new Room();

        room.setHotel(roomHotel);
        room.setRoomCapacity(roomCreateDto.getRoomCapacity());
        room.setRoomType(roomCreateDto.getRoomType());

        return room;
    }

    public RoomCreateDto toCreateDto(RoomBatchCreateDto roomBatchCreateDto) {

        RoomCreateDto roomCreateDto = new RoomCreateDto();
        roomCreateDto.setHotelId(roomBatchCreateDto.getHotelId());
        roomCreateDto.setRoomCapacity(roomBatchCreateDto.getRoomCapacity());
        roomCreateDto.setRoomType(roomBatchCreateDto.getRoomType());

        return roomCreateDto;
    }

    public void updateEntity(RoomUpdateDto roomUpdateDto, Room entity) {

        if (roomUpdateDto.getRoomCapacity() != null) {
            entity.setRoomCapacity(roomUpdateDto.getRoomCapacity());
        }

        if (roomUpdateDto.getRoomType() != null) {
            entity.setRoomType(roomUpdateDto.getRoomType());
        }
    }

    public List<RoomShortDto> toShortDtoList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toShortDto)
                .toList();
    }
}
