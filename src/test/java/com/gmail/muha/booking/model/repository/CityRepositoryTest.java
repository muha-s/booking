package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void shouldFindAllActiveCities() {
        // given
        City activeCity = new City();
        activeCity.setName("Madrid");

        City secondActiveCity = new City();
        secondActiveCity.setName("Barcelona");

        City deletedCity = new City();
        deletedCity.setName("Valencia");
        deletedCity.setDeletedAt(Instant.now());

        cityRepository.saveAll(
                List.of(activeCity, secondActiveCity, deletedCity)
        );

        // when
        List<City> result = cityRepository.findAllActive();

        // then
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(city -> city.getName().equals("Madrid")));

        assertTrue(result.stream().anyMatch(city -> city.getName().equals("Barcelona")));

        assertFalse(result.stream().anyMatch(city -> city.getName().equals("Valencia")));
    }

    @Test
    void shouldFindActiveCityById() {
        // given
        City city = new City();
        city.setName("Madrid");

        City savedCity = cityRepository.save(city);

        // when
        Optional<City> result = cityRepository.findActiveById(savedCity.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(savedCity.getId(), result.get().getId());
        assertEquals("Madrid", result.get().getName());
    }

    @Test
    void shouldNotFindDeletedCityById() {
        // given
        City city = new City();
        city.setName("Madrid");
        city.setDeletedAt(Instant.now());

        City savedCity = cityRepository.save(city);

        // when
        Optional<City> result = cityRepository.findActiveById(savedCity.getId());

        // then
        assertTrue(result.isEmpty());
    }
}