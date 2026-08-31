package com.gmail.muha.booking.service.city;

import com.gmail.muha.booking.dto.city.CityCreateDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.model.entity.City;

import java.util.List;

public interface CityService {


    City findEntityById(Long id);

    List<CityShortDto> findAll();

    CityShortDto create(CityCreateDto cityCreateDto);

    void deleteById(Long id);
}