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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = "spring.liquibase.enabled=false")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void shouldFindExpiredBookings() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user1@test.com");
        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking expiredBooking = createBooking(
                user,
                room,
                today.minusDays(10),
                today.minusDays(1),
                BookingStatus.ACTIVE
        );

        Booking endingToday = createBooking(
                user,
                room,
                today.minusDays(5),
                today,
                BookingStatus.ACTIVE
        );

        createBooking(
                user,
                room,
                today.plusDays(1),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        createBooking(
                user,
                room,
                today.minusDays(10),
                today.minusDays(1),
                BookingStatus.COMPLETED
        );

        // when
        List<Booking> result = bookingRepository.findExpiredBookings(BookingStatus.ACTIVE, today);

        // then
        assertEquals(Set.of(expiredBooking.getId(), endingToday.getId()), getBookingIds(result));
    }

    @Test
    void shouldFindBookingsForReviewRequest() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user2@test.com");
        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking bookingForReview = createBooking(
                user,
                room,
                today.minusDays(10),
                today.minusDays(5),
                BookingStatus.COMPLETED
        );

        Booking alreadyRequested = createBooking(
                user,
                room,
                today.minusDays(15),
                today.minusDays(10),
                BookingStatus.COMPLETED
        );

        alreadyRequested.setReviewRequestSentAt(Instant.parse("2026-09-01T07:00:00Z"));
        bookingRepository.save(alreadyRequested);

        createBooking(
                user,
                room,
                today.plusDays(1),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        // when
        List<Booking> result = bookingRepository.findBookingsForReviewRequest(BookingStatus.COMPLETED);

        // then
        assertEquals(1, result.size());
        assertEquals(bookingForReview.getId(), result.getFirst().getId());
    }

    @Test
    void shouldFindFutureActiveBookingsByUserId() {
        // given
        LocalDate today = LocalDate.now();

        User targetUser = createUser("user3@test.com");
        User anotherUser = createUser("user4@test.com");

        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking expectedBooking = createBooking(
                targetUser,
                room,
                today.plusDays(2),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        createBooking(
                targetUser,
                room,
                today.minusDays(5),
                today.minusDays(2),
                BookingStatus.ACTIVE
        );

        createBooking(
                targetUser,
                room,
                today.plusDays(3),
                today.plusDays(6),
                BookingStatus.COMPLETED
        );

        createBooking(
                anotherUser,
                room,
                today.plusDays(4),
                today.plusDays(7),
                BookingStatus.ACTIVE
        );

        // when
        List<Booking> result = bookingRepository.findFutureActiveBookingsByUserId(targetUser.getId());

        // then
        assertEquals(1, result.size());
        assertEquals(expectedBooking.getId(), result.getFirst().getId());
    }

    @Test
    void shouldFindFutureActiveBookingsByCityHotelAndRoom() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user5@test.com");

        City targetCity = createCity("Madrid");
        City anotherCity = createCity("Barcelona");

        Hotel targetHotel = createHotel("Target Hotel", targetCity);

        Hotel secondHotelInCity = createHotel("Second Hotel", targetCity);

        Hotel hotelInAnotherCity = createHotel("Another City Hotel", anotherCity);

        Room targetRoom = createRoom(targetHotel);
        Room secondRoom = createRoom(targetHotel);
        Room roomInSecondHotel = createRoom(secondHotelInCity);
        Room roomInAnotherCity = createRoom(hotelInAnotherCity);

        Booking targetBooking = createBooking(
                user,
                targetRoom,
                today.plusDays(2),
                today.plusDays(4),
                BookingStatus.ACTIVE
        );

        Booking bookingInSecondRoom = createBooking(
                user,
                secondRoom,
                today.plusDays(3),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        Booking bookingInSecondHotel = createBooking(
                user,
                roomInSecondHotel,
                today.plusDays(4),
                today.plusDays(6),
                BookingStatus.ACTIVE
        );

        createBooking(
                user,
                roomInAnotherCity,
                today.plusDays(5),
                today.plusDays(7),
                BookingStatus.ACTIVE
        );

        // when
        List<Booking> byRoom = bookingRepository.findFutureActiveBookingsByRoomId(targetRoom.getId());

        List<Booking> byHotel = bookingRepository.findFutureActiveBookingsByHotelId(targetHotel.getId());

        List<Booking> byCity = bookingRepository.findFutureActiveBookingsByCityId(targetCity.getId());

        // then
        assertEquals(Set.of(targetBooking.getId()), getBookingIds(byRoom));

        assertEquals(Set.of(targetBooking.getId(), bookingInSecondRoom.getId()), getBookingIds(byHotel));

        assertEquals(Set.of(
                        targetBooking.getId(),
                        bookingInSecondRoom.getId(),
                        bookingInSecondHotel.getId()
                ),
                getBookingIds(byCity)
        );
    }

    @Test
    void shouldFindBookingsByUserEmailWithCorrectOrderAndPagination() {
        // given
        LocalDate today = LocalDate.now();

        User targetUser = createUser("user6@test.com");
        User anotherUser = createUser("user7@test.com");

        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking activeLater = createBooking(
                targetUser,
                room,
                today.plusDays(5),
                today.plusDays(7),
                BookingStatus.ACTIVE
        );

        Booking activeEarlier = createBooking(
                targetUser,
                room,
                today.plusDays(2),
                today.plusDays(4),
                BookingStatus.ACTIVE
        );

        Booking completedLater = createBooking(
                targetUser,
                room,
                today.plusDays(20),
                today.plusDays(22),
                BookingStatus.COMPLETED
        );

        Booking completedEarlier = createBooking(
                targetUser,
                room,
                today.plusDays(10),
                today.plusDays(12),
                BookingStatus.COMPLETED
        );

        Booking cancelled = createBooking(
                targetUser,
                room,
                today.plusDays(30),
                today.plusDays(32),
                BookingStatus.CANCELLED
        );

        createBooking(
                anotherUser,
                room,
                today.plusDays(50),
                today.plusDays(52),
                BookingStatus.ACTIVE
        );

        // when
        Page<Booking> firstPage =
                bookingRepository.findAllByUserEmail(
                        targetUser.getEmail(),
                        PageRequest.of(0, 3)
                );

        Page<Booking> secondPage =
                bookingRepository.findAllByUserEmail(
                        targetUser.getEmail(),
                        PageRequest.of(1, 3)
                );

        // then
        assertEquals(5, firstPage.getTotalElements());
        assertEquals(2, firstPage.getTotalPages());
        assertEquals(3, firstPage.getNumberOfElements());
        assertFalse(firstPage.isLast());

        assertEquals(
                List.of(
                        activeLater.getId(),
                        activeEarlier.getId(),
                        completedLater.getId()
                ),
                firstPage.getContent()
                        .stream()
                        .map(Booking::getId)
                        .toList()
        );

        assertEquals(2, secondPage.getNumberOfElements());
        assertTrue(secondPage.isLast());

        assertEquals(
                List.of(
                        completedEarlier.getId(),
                        cancelled.getId()
                ),
                secondPage.getContent()
                        .stream()
                        .map(Booking::getId)
                        .toList()
        );
    }

    @Test
    void shouldFindBookingByIdAndUserEmail() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user8@test.com");
        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking booking = createBooking(
                user,
                room,
                today.plusDays(2),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        // when
        var result = bookingRepository.findByIdAndUserEmail(
                booking.getId(),
                user.getEmail()
        );

        // then
        assertTrue(result.isPresent());
        assertEquals(booking.getId(), result.get().getId());
    }

    @Test
    void shouldNotFindBookingForAnotherUserEmail() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user9@test.com");
        City city = createCity("Madrid");
        Hotel hotel = createHotel("Central Hotel", city);
        Room room = createRoom(hotel);

        Booking booking = createBooking(
                user,
                room,
                today.plusDays(2),
                today.plusDays(5),
                BookingStatus.ACTIVE
        );

        // when
        var result = bookingRepository.findByIdAndUserEmail(
                booking.getId(),
                "another@test.com"
        );

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindBookingsByHotelIdWithPaginationAndOrder() {
        // given
        LocalDate today = LocalDate.now();

        User user = createUser("user10@test.com");
        City city = createCity("Madrid");

        Hotel targetHotel = createHotel(
                "Target Hotel",
                city
        );

        Hotel anotherHotel = createHotel(
                "Another Hotel",
                city
        );

        Room targetRoom = createRoom(targetHotel);
        Room anotherRoom = createRoom(anotherHotel);

        Booking latestBooking = createBooking(
                user,
                targetRoom,
                today.plusDays(20),
                today.plusDays(22),
                BookingStatus.CANCELLED
        );

        Booking middleBooking = createBooking(
                user,
                targetRoom,
                today.plusDays(10),
                today.plusDays(12),
                BookingStatus.ACTIVE
        );

        Booking earliestBooking = createBooking(
                user,
                targetRoom,
                today.plusDays(2),
                today.plusDays(4),
                BookingStatus.COMPLETED
        );

        createBooking(
                user,
                anotherRoom,
                today.plusDays(30),
                today.plusDays(32),
                BookingStatus.ACTIVE
        );

        // when
        Page<Booking> firstPage =
                bookingRepository.findAllByHotelId(
                        targetHotel.getId(),
                        PageRequest.of(0, 2)
                );

        Page<Booking> secondPage =
                bookingRepository.findAllByHotelId(
                        targetHotel.getId(),
                        PageRequest.of(1, 2)
                );

        // then
        assertEquals(3, firstPage.getTotalElements());
        assertEquals(2, firstPage.getTotalPages());
        assertEquals(2, firstPage.getNumberOfElements());
        assertFalse(firstPage.isLast());

        assertEquals(
                List.of(
                        latestBooking.getId(),
                        middleBooking.getId()
                ),
                firstPage.getContent()
                        .stream()
                        .map(Booking::getId)
                        .toList()
        );

        assertEquals(1, secondPage.getNumberOfElements());
        assertTrue(secondPage.isLast());

        assertEquals(
                earliestBooking.getId(),
                secondPage.getContent().getFirst().getId()
        );
    }

    private Set<Long> getBookingIds(List<Booking> bookings) {
        return bookings.stream()
                .map(Booking::getId)
                .collect(Collectors.toSet());
    }

    private City createCity(String name) {
        City city = new City();
        city.setName(name);

        return cityRepository.save(city);
    }

    private Hotel createHotel(
            String name,
            City city
    ) {
        Hotel hotel = new Hotel();

        hotel.setName(name);
        hotel.setCity(city);
        hotel.setAddress("Test Address");
        hotel.setNumberOfStars(NumberOfStars.values()[0]);
        hotel.setBasePricePerNight(new BigDecimal("100.00"));

        return hotelRepository.save(hotel);
    }

    private Room createRoom(Hotel hotel) {
        Room room = new Room();

        room.setHotel(hotel);
        room.setRoomType(RoomType.STANDARD);
        room.setRoomCapacity(RoomCapacity.ONE_SEAT);

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

    private Booking createBooking(
            User user,
            Room room,
            LocalDate startDate,
            LocalDate endDate,
            BookingStatus status
    ) {
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