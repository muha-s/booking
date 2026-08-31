package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel.*;
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


    public HotelManagedFullDto toManagedFullDto(Hotel hotel) {
        HotelManagedFullDto hotelManagedFullDto = new HotelManagedFullDto();

        hotelManagedFullDto.setId(hotel.getId());
        hotelManagedFullDto.setName(hotel.getName());
        hotelManagedFullDto.setCity(shortDtoMapper.toCityShortDto(hotel.getCity()));
        hotelManagedFullDto.setAddress(hotel.getAddress());
        hotelManagedFullDto.setNumberOfStars(hotel.getNumberOfStars());
        hotelManagedFullDto.setRating(hotel.getRating());
        hotelManagedFullDto.setBasePricePerNight(hotel.getBasePricePerNight());
        hotelManagedFullDto.setBalance(hotel.getBalance());

        return hotelManagedFullDto;
    }

    public HotelShortDto toShortDto(Hotel hotel) {
        return shortDtoMapper.toHotelShortDto(hotel);
    }

    public HotelManagedDto toManagedDto(Hotel hotel) {
        HotelManagedDto hotelManagedDto = new HotelManagedDto();

        hotelManagedDto.setId(hotel.getId());
        hotelManagedDto.setName(hotel.getName());
        hotelManagedDto.setCity(shortDtoMapper.toCityShortDto(hotel.getCity()));
        hotelManagedDto.setAddress(hotel.getAddress());

        return hotelManagedDto;
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
