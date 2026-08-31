package com.gmail.muha.booking.service.hoteladmin.impl;

import com.gmail.muha.booking.dto.booking.BookingManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedDto;
import com.gmail.muha.booking.dto.hotel.HotelManagedFullDto;
import com.gmail.muha.booking.dto.hotel.HotelUpdateDto;
import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.dto.user.HotelAdminActivationDto;
import com.gmail.muha.booking.exception.HotelAdminActivationException;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.HotelMapper;
import com.gmail.muha.booking.mapper.RoomMapper;
import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import com.gmail.muha.booking.model.repository.UserRepository;
import com.gmail.muha.booking.service.booking.BookingService;
import com.gmail.muha.booking.service.email.EmailVerificationTokenService;
import com.gmail.muha.booking.service.hoteladmin.HotelAdminService;
import com.gmail.muha.booking.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelAdminServiceImpl implements HotelAdminService {

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final RoomService roomService;
    private final BookingService bookingService;

    @Override
    @Transactional
    public void activate(HotelAdminActivationDto hotelAdminActivationDto) {

        EmailVerificationToken verificationToken = emailVerificationTokenService.findValidByToken(
                hotelAdminActivationDto.getToken());

        User hotelAdmin = verificationToken.getUser();

        if (hotelAdmin.getRole() != UserRole.HOTEL_ADMIN) {
            throw new HotelAdminActivationException("Verification token does not belong to hotel administrator");
        }

        hotelAdmin.setPassword(passwordEncoder.encode(hotelAdminActivationDto.getPassword()));
        hotelAdmin.setEmailVerified(true);
        emailVerificationTokenService.delete(verificationToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelManagedDto> findManagedHotels(String email) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() ->
                        new NotFoundException("Hotel administrator was not found by email: " + email));

        return hotelAdmin.getManagedHotels().stream()
                .filter(hotel -> hotel.getDeletedAt() == null)
                .filter(hotel -> hotel.getCity().getDeletedAt() == null)
                .map(hotelMapper::toManagedDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelManagedFullDto findManagedHotelById(String email, Long hotelId) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() ->
                        new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Managed hotel was not found by id: " + hotelId));

        return hotelMapper.toManagedFullDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomManagedDto> findManagedRooms(String email, Long hotelId) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() -> new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Managed hotel was not found by id: " + hotelId));

        return roomMapper.toManagedDtoList(hotel.getRooms().stream()
                .filter(room -> room.getDeletedAt() == null)
                .toList());
    }

    @Override
    @Transactional
    public RoomManagedDto createManagedRoom(String email, Long hotelId, RoomManagedCreateDto roomManagedCreateDto) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() -> new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Managed hotel was not found by id: " + hotelId));

        return roomService.create(roomManagedCreateDto, hotel);
    }

    @Override
    @Transactional
    public void deleteManagedRoom(String email, Long hotelId, Long roomId) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() -> new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Managed hotel was not found by id: " + hotelId));

        boolean roomBelongsToHotel = hotel.getRooms().stream()
                .anyMatch(room -> room.getId().equals(roomId) && room.getDeletedAt() == null);

        if (!roomBelongsToHotel) {
            throw new NotFoundException("Managed room was not found by id: " + roomId);
        }
        roomService.deleteById(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingManagedDto> findManagedBookings(String email, Long hotelId) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() -> new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Managed hotel was not found by id: " + hotelId));

        return bookingService.findManagedByHotelId(hotel.getId());
    }

    @Override
    @Transactional
    public HotelManagedFullDto updateManagedHotel(String email, Long hotelId, HotelUpdateDto hotelUpdateDto) {

        User hotelAdmin = userRepository.findActiveByEmail(email)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() -> new NotFoundException("Hotel administrator was not found by email: " + email));

        Hotel hotel = hotelAdmin.getManagedHotels().stream()
                .filter(managedHotel -> managedHotel.getId().equals(hotelId))
                .filter(managedHotel -> managedHotel.getDeletedAt() == null)
                .filter(managedHotel -> managedHotel.getCity().getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Managed hotel was not found by id: " + hotelId));

        hotelMapper.updateEntity(hotelUpdateDto, hotel);

        return hotelMapper.toManagedFullDto(hotel);
    }
}