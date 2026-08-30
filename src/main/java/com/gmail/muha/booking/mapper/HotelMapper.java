package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelFullDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelMapper {

    private final ShortDtoMapper shortDtoMapper;

    public HotelMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public HotelFullDto toFullDto(Hotel hotel) {

        HotelFullDto hotelFullDto = new HotelFullDto();

        hotelFullDto.setId(hotel.getId());
        hotelFullDto.setName(hotel.getName());
        hotelFullDto.setCity(shortDtoMapper.toCityShortDto(hotel.getCity()));
        hotelFullDto.setAddress(hotel.getAddress());
        hotelFullDto.setNumberOfStars(hotel.getNumberOfStars());
        hotelFullDto.setRating(hotel.getRating());
        hotelFullDto.setBasePricePerNight(hotel.getBasePricePerNight());
        hotelFullDto.setBalance(hotel.getBalance());
        hotelFullDto.setRooms(shortDtoMapper.toRoomShortDtoList(hotel.getRooms()));

        return hotelFullDto;
    }

    public HotelShortDto toShortDto(Hotel hotel) {
        HotelShortDto hotelShortDto = new HotelShortDto();

        hotelShortDto.setId(hotel.getId());
        hotelShortDto.setName(hotel.getName());
        hotelShortDto.setCity(shortDtoMapper.toCityShortDto(hotel.getCity()));
        hotelShortDto.setAddress(hotel.getAddress());
        hotelShortDto.setNumberOfStars(hotel.getNumberOfStars());
        hotelShortDto.setRating(hotel.getRating());
        hotelShortDto.setBasePricePerNight(hotel.getBasePricePerNight());

        return hotelShortDto;
    }

    public Hotel toEntity(HotelCreateDto hotelCreateDto, City hotelCity) {
        Hotel hotel = new Hotel();

        hotel.setName(hotelCreateDto.getName());
        hotel.setCity(hotelCity);
        hotel.setAddress(hotelCreateDto.getAddress());
        hotel.setNumberOfStars(hotelCreateDto.getNumberOfStars());
        hotel.setBasePricePerNight(hotelCreateDto.getBasePricePerNight());
        return hotel;
    }

    public void updateEntity(HotelUpdateDto hotelUpdateDto, Hotel entity) {

        if (hotelUpdateDto.getName() != null) {
            entity.setName(hotelUpdateDto.getName());
        }

        if (hotelUpdateDto.getAddress() != null) {
            entity.setAddress(hotelUpdateDto.getAddress());
        }

        if (hotelUpdateDto.getNumberOfStars() != null) {
            entity.setNumberOfStars(hotelUpdateDto.getNumberOfStars());
        }

        if (hotelUpdateDto.getBasePricePerNight() != null) {
            entity.setBasePricePerNight(hotelUpdateDto.getBasePricePerNight());
        }
    }

    public List<HotelShortDto> toShortDtoList(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::toShortDto)
                .toList();
    }
}
