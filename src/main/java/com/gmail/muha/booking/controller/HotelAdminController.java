package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.booking.BookingManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedFullDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.dto.user.HotelAdminActivationDto;
import com.gmail.muha.booking.service.hoteladmin.HotelAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel-admin")
@RequiredArgsConstructor
public class HotelAdminController {

    private final HotelAdminService hotelAdminService;

    @PostMapping("/activate")
    public void activate(@Valid @RequestBody HotelAdminActivationDto hotelAdminActivationDto) {
        hotelAdminService.activate(hotelAdminActivationDto);
    }

    @GetMapping("/hotels")
    public List<HotelManagedDto> findManagedHotels(Authentication authentication) {
        return hotelAdminService.findManagedHotels(authentication.getName());
    }

    @GetMapping("/hotels/{hotelId}")
    public HotelManagedFullDto findManagedHotelById(@PathVariable Long hotelId, Authentication authentication) {
        return hotelAdminService.findManagedHotelById(authentication.getName(), hotelId);
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public List<RoomManagedDto> findManagedRooms(@PathVariable Long hotelId, Authentication authentication) {
        return hotelAdminService.findManagedRooms(authentication.getName(), hotelId);
    }

    @PostMapping("/hotels/{hotelId}/rooms")
    public RoomManagedDto createManagedRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody RoomManagedCreateDto roomManagedCreateDto,
            Authentication authentication) {

        return hotelAdminService.createManagedRoom(authentication.getName(), hotelId, roomManagedCreateDto);
    }

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    public void deleteManagedRoom(@PathVariable Long hotelId, @PathVariable Long roomId, Authentication authentication) {
        hotelAdminService.deleteManagedRoom(authentication.getName(), hotelId, roomId);
    }

    @GetMapping("/hotels/{hotelId}/bookings")
    public Page<BookingManagedDto> findManagedBookings(
            @PathVariable Long hotelId, Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {
        return hotelAdminService.findManagedBookings(authentication.getName(), hotelId, pageable);
    }

    @PutMapping("/hotels/{hotelId}")
    public HotelManagedFullDto updateManagedHotel(
            @PathVariable Long hotelId,
            @Valid @RequestBody HotelUpdateDto hotelUpdateDto,
            Authentication authentication) {

        return hotelAdminService.updateManagedHotel(authentication.getName(), hotelId, hotelUpdateDto);
    }
}