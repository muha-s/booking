package com.gmail.muha.booking.service.superadmin.impl;

import com.gmail.muha.booking.dto.user.HotelAdminCreateDto;
import com.gmail.muha.booking.dto.user.HotelAdminDto;
import com.gmail.muha.booking.dto.user.UserSummaryDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.UserAlreadyExistsException;
import com.gmail.muha.booking.mapper.UserMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.UserRepository;
import com.gmail.muha.booking.service.booking.BookingCancellationService;
import com.gmail.muha.booking.service.email.EmailService;
import com.gmail.muha.booking.service.email.EmailVerificationTokenService;
import com.gmail.muha.booking.service.hotel.HotelService;
import com.gmail.muha.booking.service.superadmin.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;
    private final HotelService hotelService;
    private final BookingRepository bookingRepository;
    private final BookingCancellationService bookingCancellationService;

    @Override
    @Transactional
    public HotelAdminDto createHotelAdmin(HotelAdminCreateDto hotelAdminCreateDto) {
        String email = hotelAdminCreateDto.getEmail().trim();

        if (userRepository.existsByEmailIgnoreCase(email) || userRepository.existsByPendingEmailIgnoreCase(email)) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User hotelAdmin = userMapper.toEntity(hotelAdminCreateDto);

        hotelAdmin.setEmail(email);
        hotelAdmin.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        hotelAdmin.setEmailVerified(false);

        User savedHotelAdmin = userRepository.save(hotelAdmin);

        EmailVerificationToken verificationToken = emailVerificationTokenService.create(savedHotelAdmin);

        String activationUrl = "http://localhost:4200/hotel-admin/activate?token=" + verificationToken.getToken();

        emailService.sendEmail(
                savedHotelAdmin.getEmail(),
                "Activate hotel administrator account",
                """
                Your hotel administrator account has been created.

                To activate your account and set your password, follow this link:
                %s
                """.formatted(activationUrl)
        );

        return userMapper.toHotelAdminDto(savedHotelAdmin);
    }

    @Override
    public List<HotelAdminDto> findAllHotelAdmins() {
        return userRepository.findAllActiveByRole(UserRole.HOTEL_ADMIN).stream()
                .map(userMapper::toHotelAdminDto)
                .toList();
    }

    @Override
    @Transactional
    public HotelAdminDto assignHotelToAdmin(Long hotelAdminId, Long hotelId) {

        User hotelAdmin = userRepository.findActiveById(hotelAdminId)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() ->
                        new NotFoundException("Hotel administrator was not found by id: " + hotelAdminId));

        Hotel hotel = hotelService.findEntityById(hotelId);

        hotel.getAdmins().add(hotelAdmin);
        hotelAdmin.getManagedHotels().add(hotel);

        return userMapper.toHotelAdminDto(hotelAdmin);
    }

    @Override
    @Transactional
    public HotelAdminDto unassignHotelFromAdmin(Long hotelAdminId, Long hotelId) {

        User hotelAdmin = userRepository.findActiveById(hotelAdminId)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() ->
                        new NotFoundException("Hotel administrator was not found by id: " + hotelAdminId));

        Hotel hotel = hotelService.findEntityById(hotelId);

        hotel.getAdmins().remove(hotelAdmin);
        hotelAdmin.getManagedHotels().remove(hotel);

        return userMapper.toHotelAdminDto(hotelAdmin);
    }

    @Override
    @Transactional
    public void deleteHotelAdmin(Long hotelAdminId) {

        User hotelAdmin = userRepository.findActiveById(hotelAdminId)
                .filter(user -> user.getRole() == UserRole.HOTEL_ADMIN)
                .orElseThrow(() ->
                        new NotFoundException("Hotel administrator was not found by id: " + hotelAdminId));

        for (Hotel hotel : new HashSet<>(hotelAdmin.getManagedHotels())) {
            hotel.getAdmins().remove(hotelAdmin);
        }

        hotelAdmin.getManagedHotels().clear();
        hotelAdmin.setDeletedAt(Instant.now());
    }

    @Override
    public List<UserSummaryDto> findAllUsers() {
        return userMapper.toUserSummaryDtoList(userRepository.findAllByRoleOrderByIdAsc(UserRole.USER));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findActiveById(userId)
                .filter(currentUser -> currentUser.getRole() == UserRole.USER)
                .orElseThrow(() ->
                        new NotFoundException("User was not found by id: " + userId));

        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByUserId(userId);

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByAdministration(booking);
        }
        user.setDeletedAt(Instant.now());
    }
}