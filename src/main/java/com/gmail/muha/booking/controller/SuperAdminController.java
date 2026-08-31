package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.user.HotelAdminCreateDto;
import com.gmail.muha.booking.dto.user.HotelAdminDto;
import com.gmail.muha.booking.dto.user.UserSummaryDto;
import com.gmail.muha.booking.service.superadmin.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PostMapping("/hotel-admins")
    public HotelAdminDto createHotelAdmin(
            @Valid @RequestBody HotelAdminCreateDto hotelAdminCreateDto) {

        return superAdminService.createHotelAdmin(hotelAdminCreateDto);
    }

    @GetMapping("/hotel-admins")
    public List<HotelAdminDto> findAllHotelAdmins() {
        return superAdminService.findAllHotelAdmins();
    }

    @PutMapping("/hotel-admins/{hotelAdminId}/hotels/{hotelId}")
    public HotelAdminDto assignHotelToAdmin(@PathVariable Long hotelAdminId, @PathVariable Long hotelId) {
        return superAdminService.assignHotelToAdmin(hotelAdminId, hotelId);
    }

    @DeleteMapping("/hotel-admins/{hotelAdminId}/hotels/{hotelId}")
    public HotelAdminDto unassignHotelFromAdmin(@PathVariable Long hotelAdminId, @PathVariable Long hotelId) {
        return superAdminService.unassignHotelFromAdmin(hotelAdminId, hotelId);
    }

    @DeleteMapping("/hotel-admins/{hotelAdminId}")
    public void deleteHotelAdmin(@PathVariable Long hotelAdminId) {
        superAdminService.deleteHotelAdmin(hotelAdminId);
    }

    @GetMapping("/users")
    public List<UserSummaryDto> findAllUsers() {
        return superAdminService.findAllUsers();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        superAdminService.deleteUser(userId);
    }
}