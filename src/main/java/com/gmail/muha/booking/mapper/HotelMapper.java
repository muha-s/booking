package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.hotel_dto.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelResponseDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelUpdateDto;
import com.gmail.muha.booking.entity.City;
import com.gmail.muha.booking.entity.Hotel;

public class HotelMapper {

    private HotelMapper() {
    }

    public Hotel toEntity(HotelCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new Hotel(dto.getName(), dto.getCityId());
    }

    public HotelResponseDto toResponseDto(Hotel hotel, City city) {
        if (hotel == null) {
            return null;
        }
        HotelResponseDto dto = new HotelResponseDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setCityName(city.getName());
        return dto;
    }

    public HotelUpdateDto toUpdateDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }
        return new HotelUpdateDto(hotel.getId(), hotel.getName());
    }

    private static class Holder {
        private static final HotelMapper INSTANCE = new HotelMapper();
    }

    public static HotelMapper getInstance() {
        return HotelMapper.Holder.INSTANCE;
    }
}
