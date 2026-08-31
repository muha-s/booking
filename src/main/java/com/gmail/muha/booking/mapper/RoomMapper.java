package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.room.RoomAvailableDto;
import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RoomMapper {


    public RoomManagedDto toManagedDto(Room room) {
        RoomManagedDto roomManagedDto = new RoomManagedDto();

        roomManagedDto.setId(room.getId());
        roomManagedDto.setRoomCapacity(room.getRoomCapacity());
        roomManagedDto.setRoomType(room.getRoomType());

        return roomManagedDto;
    }

    public RoomAvailableDto toAvailableDto(Room room) {

        RoomAvailableDto roomAvailableDto = new RoomAvailableDto();

        roomAvailableDto.setId(room.getId());
        roomAvailableDto.setHotelId(room.getHotel().getId());
        roomAvailableDto.setHotelName(room.getHotel().getName());
        roomAvailableDto.setHotelAddress(room.getHotel().getAddress());
        roomAvailableDto.setHotelStars(room.getHotel().getNumberOfStars());
        roomAvailableDto.setHotelRating(room.getHotel().getRating());
        roomAvailableDto.setRoomType(room.getRoomType());
        roomAvailableDto.setRoomCapacity(room.getRoomCapacity());

        BigDecimal pricePerNight =
                room.getHotel().getBasePricePerNight()
                        .multiply(BigDecimal.valueOf(room.getRoomType().getCostFactor()))
                        .multiply(BigDecimal.valueOf(room.getRoomCapacity().getCostFactor()));

        roomAvailableDto.setPricePerNight(pricePerNight);

        return roomAvailableDto;
    }

    public Room toEntity(RoomManagedCreateDto roomManagedCreateDto, Hotel hotel) {
        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomCapacity(roomManagedCreateDto.getRoomCapacity());
        room.setRoomType(roomManagedCreateDto.getRoomType());
        return room;
    }


    public List<RoomAvailableDto> toAvailableDtoList(List<Room> rooms) {
        return rooms.stream().map(this::toAvailableDto).toList();
    }

    public List<RoomManagedDto> toManagedDtoList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toManagedDto)
                .toList();
    }
}
