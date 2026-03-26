package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.hotel_dto.HotelCreateDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelResponseDto;
import com.gmail.muha.booking.dto.hotel_dto.HotelUpdateDto;
import com.gmail.muha.booking.service.HotelService;

import java.util.List;

public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;

    }

    public HotelResponseDto createHotel(String name, Long cityId) {
        HotelCreateDto createHotelDto = new HotelCreateDto(name, cityId);
        return service.createHotel(createHotelDto);
    }

    public HotelResponseDto getHotel(Long id) {
        return service.getHotel(id);
    }

    public List<HotelResponseDto> getHotels() {
        return service.getAllHotels();
    }

    public HotelResponseDto updateHotel(Long id, String name) {
        HotelUpdateDto hotelUpdateDto = new HotelUpdateDto(id, name);
        return service.updateHotel(hotelUpdateDto);

    }

    public void deleteHotel(Long id) {
        service.deleteHotel(id);
    }
}
