package com.gmail.muha.booking.service.hotel;

import com.gmail.muha.booking.dto.hotel.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.model.entity.Hotel;

import java.util.List;

public interface HotelService {


    Hotel findEntityById(Long id);

    List<HotelShortDto> findAll();

    HotelShortDto create(HotelCreateDto hotelCreateDto);

    void deleteById(Long id);
}
