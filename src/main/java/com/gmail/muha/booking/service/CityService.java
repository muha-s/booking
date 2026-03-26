package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.city_dto.CityCreateDto;
import com.gmail.muha.booking.dto.city_dto.CityResponseDto;
import com.gmail.muha.booking.entity.City;

import java.util.List;

public interface CityService {

    CityResponseDto createCity(CityCreateDto dto);

    CityResponseDto getCity(Long id);

    List<CityResponseDto> getAllCities();

    void deleteCity(Long id);

    City findCityEntityById(Long id);
}
