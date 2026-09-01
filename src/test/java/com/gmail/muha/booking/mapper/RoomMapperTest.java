package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.room.RoomAvailableDto;
import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RoomMapperTest {

    private final RoomMapper roomMapper = new RoomMapper();

    @Test
    void shouldMapRoomToManagedDto() {
        // given
        RoomCapacity roomCapacity = RoomCapacity.values()[0];
        RoomType roomType = RoomType.values()[0];

        Room room = new Room();
        room.setId(1L);
        room.setRoomCapacity(roomCapacity);
        room.setRoomType(roomType);

        // when
        RoomManagedDto result = roomMapper.toManagedDto(room);

        // then
        assertEquals(1L, result.getId());
        assertEquals(roomCapacity, result.getRoomCapacity());
        assertEquals(roomType, result.getRoomType());
    }

    @Test
    void shouldMapRoomToAvailableDto() {
        // given
        NumberOfStars stars = NumberOfStars.FOUR_STARS;
        RoomCapacity roomCapacity = RoomCapacity.TWO_SEAT;
        RoomType roomType = RoomType.LUXURY;

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setAddress("Main Street 10");
        hotel.setNumberOfStars(stars);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));

        Room room = new Room();
        room.setId(1L);
        room.setHotel(hotel);
        room.setRoomCapacity(roomCapacity);
        room.setRoomType(roomType);

        BigDecimal expectedPrice = hotel.getBasePricePerNight()
                .multiply(BigDecimal.valueOf(roomType.getCostFactor()))
                .multiply(BigDecimal.valueOf(roomCapacity.getCostFactor()));

        // when
        RoomAvailableDto result = roomMapper.toAvailableDto(room);

        // then
        assertEquals(1L, result.getId());
        assertEquals(10L, result.getHotelId());
        assertEquals("Central Hotel", result.getHotelName());
        assertEquals("Main Street 10", result.getHotelAddress());
        assertEquals(stars, result.getHotelStars());
        assertEquals(roomType, result.getRoomType());
        assertEquals(roomCapacity, result.getRoomCapacity());
        assertEquals(expectedPrice, result.getPricePerNight());
    }

    @Test
    void shouldMapRoomManagedCreateDtoToEntity() {
        // given
        RoomCapacity roomCapacity = RoomCapacity.values()[0];
        RoomType roomType = RoomType.values()[0];

        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");

        RoomManagedCreateDto roomManagedCreateDto = new RoomManagedCreateDto();
        roomManagedCreateDto.setRoomCapacity(roomCapacity);
        roomManagedCreateDto.setRoomType(roomType);

        // when
        Room result = roomMapper.toEntity(roomManagedCreateDto, hotel);

        // then
        assertSame(hotel, result.getHotel());
        assertEquals(roomCapacity, result.getRoomCapacity());
        assertEquals(roomType, result.getRoomType());
    }

    @Test
    void shouldMapRoomsToAvailableDtoList() {
        // given
        Hotel hotel = new Hotel();
        hotel.setId(10L);
        hotel.setName("Central Hotel");
        hotel.setAddress("Main Street 10");
        hotel.setNumberOfStars(NumberOfStars.values()[0]);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));

        Room firstRoom = new Room();
        firstRoom.setId(1L);
        firstRoom.setHotel(hotel);
        firstRoom.setRoomCapacity(RoomCapacity.values()[0]);
        firstRoom.setRoomType(RoomType.values()[0]);

        Room secondRoom = new Room();
        secondRoom.setId(2L);
        secondRoom.setHotel(hotel);
        secondRoom.setRoomCapacity(RoomCapacity.values()[0]);
        secondRoom.setRoomType(RoomType.values()[0]);

        List<Room> rooms = List.of(firstRoom, secondRoom);

        // when
        List<RoomAvailableDto> result = roomMapper.toAvailableDtoList(rooms);

        // then
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Central Hotel", result.get(0).getHotelName());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Central Hotel", result.get(1).getHotelName());
    }

    @Test
    void shouldMapRoomsToManagedDtoList() {
        // given
        RoomCapacity roomCapacity = RoomCapacity.values()[0];
        RoomType roomType = RoomType.values()[0];

        Room firstRoom = new Room();
        firstRoom.setId(1L);
        firstRoom.setRoomCapacity(roomCapacity);
        firstRoom.setRoomType(roomType);

        Room secondRoom = new Room();
        secondRoom.setId(2L);
        secondRoom.setRoomCapacity(roomCapacity);
        secondRoom.setRoomType(roomType);

        List<Room> rooms = List.of(firstRoom, secondRoom);

        // when
        List<RoomManagedDto> result = roomMapper.toManagedDtoList(rooms);

        // then
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals(roomCapacity, result.get(0).getRoomCapacity());
        assertEquals(roomType, result.get(0).getRoomType());

        assertEquals(2L, result.get(1).getId());
        assertEquals(roomCapacity, result.get(1).getRoomCapacity());
        assertEquals(roomType, result.get(1).getRoomType());
    }
}