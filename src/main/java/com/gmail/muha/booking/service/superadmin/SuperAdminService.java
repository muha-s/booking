package com.gmail.muha.booking.service.superadmin;

import com.gmail.muha.booking.dto.user.HotelAdminCreateDto;
import com.gmail.muha.booking.dto.user.HotelAdminDto;
import com.gmail.muha.booking.dto.user.UserSummaryDto;

import java.util.List;

public interface SuperAdminService {

    HotelAdminDto createHotelAdmin(HotelAdminCreateDto hotelAdminCreateDto);

    List<HotelAdminDto> findAllHotelAdmins();

    HotelAdminDto assignHotelToAdmin(Long hotelAdminId, Long hotelId);

    HotelAdminDto unassignHotelFromAdmin(Long hotelAdminId, Long hotelId);

    void deleteHotelAdmin(Long hotelAdminId);

    List<UserSummaryDto> findAllUsers();

    void deleteUser(Long userId);
}