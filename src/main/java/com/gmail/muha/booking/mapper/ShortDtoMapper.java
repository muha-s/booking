package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.booking.BookingShortDto;
import com.gmail.muha.booking.dto.city.CityShortDto;
import com.gmail.muha.booking.dto.hotel.HotelShortDto;
import com.gmail.muha.booking.dto.room.RoomShortDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.model.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
 class ShortDtoMapper {


    public BookingShortDto toBookingShortDto(Booking booking) {

        BookingShortDto bookingShortDto = new BookingShortDto();

        bookingShortDto.setId(booking.getId());
        bookingShortDto.setUser(toUserShortDto(booking.getUser()));
        bookingShortDto.setRoom(toRoomShortDto(booking.getRoom()));
        bookingShortDto.setStartDate(booking.getStartDate());
        bookingShortDto.setEndDate(booking.getEndDate());
        bookingShortDto.setStatus(booking.getStatus());
        bookingShortDto.setTotalPrice(booking.getTotalPrice());

        return bookingShortDto;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();

        userShortDto.setId(user.getId());
        userShortDto.setRole(user.getRole());
        userShortDto.setFirstName(user.getFirstName());
        userShortDto.setLastName(user.getLastName());
        userShortDto.setPhone(user.getPhone());
        return userShortDto;
    }

    public RoomShortDto toRoomShortDto(Room room) {
        RoomShortDto roomShortDto = new RoomShortDto();

        roomShortDto.setId(room.getId());
        roomShortDto.setHotel(toHotelShortDto(room.getHotel()));
        roomShortDto.setRoomCapacity(room.getRoomCapacity());
        roomShortDto.setRoomType(room.getRoomType());
        return roomShortDto;
    }

    public HotelShortDto toHotelShortDto(Hotel hotel) {
        HotelShortDto hotelShortDto = new HotelShortDto();

        hotelShortDto.setId(hotel.getId());
        hotelShortDto.setName(hotel.getName());
        hotelShortDto.setCity(toCityShortDto(hotel.getCity()));
        hotelShortDto.setAddress(hotel.getAddress());
        hotelShortDto.setNumberOfStars(hotel.getNumberOfStars());
        hotelShortDto.setRating(hotel.getRating());
        hotelShortDto.setBasePricePerNight(hotel.getBasePricePerNight());
        hotelShortDto.setBalance(hotel.getBalance());

        return hotelShortDto;
    }

    public CityShortDto toCityShortDto(City city) {
        CityShortDto cityShortDto = new CityShortDto();

        cityShortDto.setId(city.getId());
        cityShortDto.setName(city.getName());

        return cityShortDto;
    }

    public List<BookingShortDto> toBookingShortDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingShortDto)
                .toList();
    }

    public List<HotelShortDto> toHotelShortDtoList(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::toHotelShortDto)
                .toList();
    }

    public Set<HotelShortDto> toHotelShortDtoSet(Set<Hotel> hotels) {
        return hotels.stream()
                .map(this::toHotelShortDto)
                .collect(Collectors.toSet());
    }

    public List<RoomShortDto> toRoomShortDtoList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toRoomShortDto)
                .toList();
    }
}
