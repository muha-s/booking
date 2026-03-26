package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.city_dto.CityCreateDto;
import com.gmail.muha.booking.dto.city_dto.CityResponseDto;
import com.gmail.muha.booking.service.CityService;

import java.util.List;

public class CityController {

    private final CityService service;

    public CityController(CityService service) {
        this.service = service;

    }

    public CityResponseDto createCity(String name) {
        CityCreateDto cityCreateDto = new CityCreateDto(name);
        return service.createCity(cityCreateDto);
    }

    public CityResponseDto getCity(Long id) {
        return service.getCity(id);
    }

    public List<CityResponseDto> getCities() {
        return service.getAllCities();
    }

    public void deleteCity(Long id) {
        service.deleteCity(id);
    }
}
