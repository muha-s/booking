package com.gmail.muha.booking.service.hoteladmin;

import com.gmail.muha.booking.dto.booking.BookingManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedFullDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.dto.user.HotelAdminActivationDto;

import java.util.List;

public interface HotelAdminService {

    void activate(HotelAdminActivationDto hotelAdminActivationDto);

    List<HotelManagedDto> findManagedHotels(String email);

    HotelManagedFullDto findManagedHotelById(String email, Long hotelId);

    List<RoomManagedDto> findManagedRooms(String email, Long hotelId);

    RoomManagedDto createManagedRoom(String email, Long hotelId, RoomManagedCreateDto roomManagedCreateDto);

    void deleteManagedRoom(String email, Long hotelId, Long roomId);

    List<BookingManagedDto> findManagedBookings(String email, Long hotelId);

    HotelManagedFullDto updateManagedHotel(String email, Long hotelId, HotelUpdateDto hotelUpdateDto);
}