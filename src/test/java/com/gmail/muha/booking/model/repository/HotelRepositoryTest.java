package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void shouldFindAllActiveHotels() {
        // given
        City activeCity = createCity("Madrid", null);
        City deletedCity = createCity("Barcelona", Instant.now());

        Hotel activeHotel = createHotel("Active Hotel", activeCity, null);

        createHotel("Deleted Hotel", activeCity, Instant.now());

        createHotel("Hotel In Deleted City", deletedCity, null);

        // when
        List<Hotel> result = hotelRepository.findAllActive();

        // then
        assertEquals(1, result.size());
        assertEquals(activeHotel.getId(), result.getFirst().getId());
        assertEquals("Active Hotel", result.getFirst().getName());
    }

    @Test
    void shouldFindActiveHotelById() {
        // given
        City city = createCity("Madrid", null);

        Hotel hotel = createHotel("Central Hotel", city, null);

        // when
        Optional<Hotel> result = hotelRepository.findActiveById(hotel.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(hotel.getId(), result.get().getId());
        assertEquals("Central Hotel", result.get().getName());
    }

    @Test
    void shouldNotFindDeletedHotelById() {
        // given
        City city = createCity("Madrid", null);

        Hotel hotel = createHotel("Central Hotel", city, Instant.now());

        // when
        Optional<Hotel> result = hotelRepository.findActiveById(hotel.getId());

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotFindHotelWhenCityIsDeleted() {
        // given
        City city = createCity("Madrid", Instant.now());

        Hotel hotel = createHotel("Central Hotel", city, null);

        // when
        Optional<Hotel> result = hotelRepository.findActiveById(hotel.getId());

        // then
        assertTrue(result.isEmpty());
    }

    private City createCity(String name, Instant deletedAt) {
        City city = new City();
        city.setName(name);
        city.setDeletedAt(deletedAt);

        return cityRepository.save(city);
    }

    private Hotel createHotel(
            String name,
            City city,
            Instant deletedAt
    ) {
        Hotel hotel = new Hotel();

        hotel.setName(name);
        hotel.setCity(city);
        hotel.setAddress("Test Address");
        hotel.setNumberOfStars(NumberOfStars.values()[0]);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));
        hotel.setDeletedAt(deletedAt);

        return hotelRepository.save(hotel);
    }
}