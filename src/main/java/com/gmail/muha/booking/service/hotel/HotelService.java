package com.gmail.muha.booking.service.hotel;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelFullDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.model.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelFullDto findById(Long id);

    Hotel findEntityById(Long id);

    List<HotelShortDto> findAll();

    HotelFullDto create(HotelCreateDto hotelCreateDto);

    HotelFullDto update(Long id, HotelUpdateDto hotelUpdateDto);

    void deleteById(Long id);
}
