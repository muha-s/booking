package com.gmail.muha.booking.model.repository;

import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.City;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.BookingStatus;
import com.gmail.muha.booking.model.entity.enums.NumberOfStars;
import com.gmail.muha.booking.model.entity.enums.RoomCapacity;
import com.gmail.muha.booking.model.entity.enums.RoomType;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldFindActiveRoomById() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);

        Room room = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        // when
        var result = roomRepository.findActiveById(room.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(room.getId(), result.get().getId());
    }

    @Test
    void shouldNotFindRoomWhenRoomHotelOrCityIsDeleted() {
        // given
        City activeCity = createCity("Madrid", null);
        City deletedCity = createCity("Barcelona", Instant.now());

        Hotel activeHotel = createHotel("Active Hotel", activeCity, null);

        Hotel deletedHotel = createHotel("Deleted Hotel", activeCity, Instant.now());

        Hotel hotelInDeletedCity = createHotel("Hotel In Deleted City", deletedCity, null);

        Room deletedRoom = createRoom(activeHotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, Instant.now());

        Room roomInDeletedHotel = createRoom(deletedHotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room roomInDeletedCity = createRoom(hotelInDeletedCity, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        // when / then
        assertTrue(roomRepository.findActiveById(deletedRoom.getId()).isEmpty());

        assertTrue(roomRepository.findActiveById(roomInDeletedHotel.getId()).isEmpty());

        assertTrue(roomRepository.findActiveById(roomInDeletedCity.getId()).isEmpty());
    }

    @Test
    void shouldFindSuitableRoomsAndExcludeRoomWithOverlappingActiveBooking() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);
        User user = createUser("user1@test.com");

        Room freeRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room occupiedRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room roomWithCancelledBooking = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        createBooking(
                user,
                occupiedRoom,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                BookingStatus.ACTIVE
        );

        createBooking(
                user,
                roomWithCancelledBooking,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                BookingStatus.CANCELLED
        );

        // when
        List<Room> result = roomRepository.findSuitableActiveRooms(
                hotel.getId(),
                RoomType.STANDARD,
                RoomCapacity.ONE_SEAT,
                LocalDate.of(2026, 9, 12),
                LocalDate.of(2026, 9, 14),
                BookingStatus.ACTIVE
        );

        // then
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(room -> room.getId().equals(freeRoom.getId())));

        assertTrue(result.stream().anyMatch(room -> room.getId().equals(roomWithCancelledBooking.getId())));

        assertFalse(result.stream().anyMatch(room -> room.getId().equals(occupiedRoom.getId())));
    }

    @Test
    void shouldIgnoreCurrentBookingWhenFindingSuitableRoomForUpdate() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);
        User user = createUser("user2@test.com");

        Room currentRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room occupiedRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Booking currentBooking = createBooking(
                user,
                currentRoom,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                BookingStatus.ACTIVE
        );

        createBooking(
                user,
                occupiedRoom,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                BookingStatus.ACTIVE
        );

        // when
        List<Room> result = roomRepository.findSuitableActiveRoomsForUpdate(
                hotel.getId(),
                RoomType.STANDARD,
                RoomCapacity.ONE_SEAT,
                LocalDate.of(2026, 9, 11),
                LocalDate.of(2026, 9, 14),
                BookingStatus.ACTIVE,
                currentBooking.getId()
        );

        // then
        assertTrue(result.stream().anyMatch(room -> room.getId().equals(currentRoom.getId())));

        assertFalse(result.stream().anyMatch(room -> room.getId().equals(occupiedRoom.getId())));
    }

    @Test
    void shouldCheckIfMatchingActiveRoomExists() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);

        createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        createRoom(hotel, RoomType.COMFORT, RoomCapacity.TWO_SEAT, Instant.now());

        // when
        boolean existingRoom = roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                RoomType.STANDARD,
                RoomCapacity.ONE_SEAT
        );

        boolean deletedRoom = roomRepository.existsRoomByHotelIdAndRoomTypeAndRoomCapacity(
                hotel.getId(),
                RoomType.COMFORT,
                RoomCapacity.TWO_SEAT
        );

        // then
        assertTrue(existingRoom);
        assertFalse(deletedRoom);
    }

    @Test
    void shouldFindAvailableRoomsByCityTypeCapacityAndDates() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);
        User user = createUser("user3@test.com");

        Room availableRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room occupiedRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        createRoom(hotel, RoomType.COMFORT, RoomCapacity.ONE_SEAT, null);

        createBooking(
                user,
                occupiedRoom,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 20),
                BookingStatus.ACTIVE
        );

        // when
        List<Room> result = roomRepository.findAvailableRooms(
                city.getId(),
                LocalDate.of(2026, 9, 12),
                LocalDate.of(2026, 9, 15),
                RoomCapacity.ONE_SEAT,
                RoomType.STANDARD
        );

        // then
        assertEquals(1, result.size());
        assertEquals(availableRoom.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindAvailableRoomsWhenOptionalFiltersAreNull() {
        // given
        City city = createCity("Madrid", null);
        Hotel hotel = createHotel("Central Hotel", city, null);

        Room standardRoom = createRoom(hotel, RoomType.STANDARD, RoomCapacity.ONE_SEAT, null);

        Room comfortRoom = createRoom(hotel, RoomType.COMFORT, RoomCapacity.TWO_SEAT, null);

        // when
        List<Room> result = roomRepository.findAvailableRooms(
                city.getId(),
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                null,
                null
        );

        // then
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(room -> room.getId().equals(standardRoom.getId())));

        assertTrue(result.stream().anyMatch(room -> room.getId().equals(comfortRoom.getId())));
    }

    private City createCity(String name, Instant deletedAt) {
        City city = new City();
        city.setName(name);
        city.setDeletedAt(deletedAt);

        return cityRepository.save(city);
    }

    private Hotel createHotel(String name, City city, Instant deletedAt) {
        Hotel hotel = new Hotel();

        hotel.setName(name);
        hotel.setCity(city);
        hotel.setAddress("Test Address");
        hotel.setNumberOfStars(NumberOfStars.values()[0]);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));
        hotel.setDeletedAt(deletedAt);

        return hotelRepository.save(hotel);
    }

    private Room createRoom(Hotel hotel, RoomType roomType, RoomCapacity roomCapacity, Instant deletedAt) {
        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomType(roomType);
        room.setRoomCapacity(roomCapacity);
        room.setDeletedAt(deletedAt);

        return roomRepository.save(room);
    }

    private User createUser(String email) {
        User user = new User();

        user.setRole(UserRole.USER);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+34123456789");
        user.setEmail(email);
        user.setPassword("password");
        user.setBalance(new BigDecimal("1000.00"));
        user.setEmailVerified(true);

        return userRepository.save(user);
    }

    private Booking createBooking(User user, Room room, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus(status);
        booking.setTotalPrice(new BigDecimal("200.00"));

        return bookingRepository.save(booking);
    }
}