package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.hotel_dto.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelResponseDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelUpdateDto;
import com.gmail.muha.booking.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelResponseDto createHotel(HotelCreateDto dto);

    HotelResponseDto getHotel(Long id);

    List<HotelResponseDto> getAllHotels();

    void deleteHotel(Long id);

    Hotel findHotelEntityById(Long id);

    HotelResponseDto updateHotel(HotelUpdateDto hotelUpdateDto);
}
