package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityFullDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.city.CityUpdateDto;
import com.gmail.muha.booking.model.entity.City;

import java.util.List;

public interface CityService {

    CityFullDto findById(Long id);

    City findEntityById(Long id);

    List<CityShortDto> findAll();

    CityFullDto create(CityCreateDto cityCreateDto);

    CityFullDto update(Long id, CityUpdateDto cityUpdateDto);

    void deleteById(Long id);
}