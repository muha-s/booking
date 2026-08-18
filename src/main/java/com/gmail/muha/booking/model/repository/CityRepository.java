package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("""
           select city
           from City city
           where city.deletedAt is null
           """)
    List<City> findAllActive();

    @Query("""
           select city
           from City city
           where city.id = :id
             and city.deletedAt is null
           """)
    Optional<City> findActiveById(Long id);
}