package com.gmail.muha.booking.repository;

import com.gmail.muha.booking.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityRepository {

    City save(City city);

    Optional<City> findById(Long id);

    List<City> findAll();

    void deleteById(Long id);

}
