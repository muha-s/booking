package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.room.RoomShortDto;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
 class ShortDtoMapper {


    public RoomShortDto toRoomShortDto(Room room) {
        RoomShortDto roomShortDto = new RoomShortDto();

        roomShortDto.setId(room.getId());
        roomShortDto.setHotel(toHotelShortDto(room.getHotel()));
        roomShortDto.setRoomCapacity(room.getRoomCapacity());
        roomShortDto.setRoomType(room.getRoomType());
        return roomShortDto;
    }

    public HotelShortDto toHotelShortDto(Hotel hotel) {
        HotelShortDto hotelShortDto = new HotelShortDto();

        hotelShortDto.setId(hotel.getId());
        hotelShortDto.setName(hotel.getName());
        hotelShortDto.setCity(toCityShortDto(hotel.getCity()));
        hotelShortDto.setAddress(hotel.getAddress());
        hotelShortDto.setNumberOfStars(hotel.getNumberOfStars());
        hotelShortDto.setRating(hotel.getRating());
        hotelShortDto.setBasePricePerNight(hotel.getBasePricePerNight());

        return hotelShortDto;
    }

    public CityShortDto toCityShortDto(City city) {
        CityShortDto cityShortDto = new CityShortDto();

        cityShortDto.setId(city.getId());
        cityShortDto.setName(city.getName());

        return cityShortDto;
    }

    public Set<HotelShortDto> toHotelShortDtoSet(Set<Hotel> hotels) {
        return hotels.stream()
                .map(this::toHotelShortDto)
                .collect(Collectors.toSet());
    }

}
