package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.city_dto.CityCreateDto;
import com.gmail.muha.booking.dto.city_dto.CityResponseDto;
import com.gmail.muha.booking.entity.City;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.CityMapper;
import com.gmail.muha.booking.repository.CityRepository;
import com.gmail.muha.booking.service.CityService;

import java.util.List;

public class CityServiceImpl implements CityService {
    private final CityRepository repository;
    private final CityMapper mapper;

    public CityServiceImpl(CityRepository repository, CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CityResponseDto createCity(CityCreateDto dto) {
        City city = mapper.toEntity(dto);

        city = repository.save(city);
        return mapper.toDto(city);
    }

    @Override
    public CityResponseDto getCity(Long id) {
        City city = findCityEntityById(id);
        return mapper.toDto(city);
    }

    @Override
    public List<CityResponseDto> getAllCities() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public void deleteCity(Long id) {
        findCityEntityById(id);
        repository.deleteById(id);
    }

    @Override
    public City findCityEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("City not found with id: " + id));
    }
}
