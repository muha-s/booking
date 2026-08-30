package com.gmail.muha.booking.service.user.impl;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.UserMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.UserRepository;
import com.gmail.muha.booking.service.booking.BookingCancellationService;
import com.gmail.muha.booking.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final BookingCancellationService bookingCancellationService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserFullDto findById(Long id) {
        return userMapper.toFullDto(findEntityById(id));
    }

    @Override
    public User findEntityById(Long id) {
        return userRepository.findActiveById(id).orElseThrow(() ->
                new NotFoundException("User was not found by id: " + id));
    }

    @Override
    public List<UserShortDto> findAll() {
        return userMapper.toShortDtoList(userRepository.findAllActive());
    }

    @Override
    public UserFullDto update(Long id, UserUpdateDto userUpdateDto) {
        User updatingUser = findEntityById(id);
        userMapper.updateEntity(userUpdateDto, updatingUser);
        return userMapper.toFullDto(userRepository.save(updatingUser));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByUserId(id);

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByUser(booking);
        }
        User user = findEntityById(id);
        user.setDeletedAt(Instant.now());
    }

    @Override
    public UserProfileDto findProfileByEmail(String email) {
        User user = userRepository.findActiveByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User was not found by email: " + email
                        ));

        return userMapper.toUserProfileDto(user);
    }

    @Override
    public User findEntityByEmail(String email) {
        return userRepository.findActiveByEmail(email).orElseThrow(() ->
                new NotFoundException("User was not found by email: " + email));
    }

    @Transactional
    @Override
    public UserProfileDto updateProfile(String email, UserUpdateDto userUpdateDto) {

        User updatingUser = findEntityByEmail(email);

        userMapper.updateEntity(userUpdateDto, updatingUser);

        return userMapper.toUserProfileDto(updatingUser);
    }

    @Transactional
    @Override
    public void updatePassword(String email, UserPasswordUpdateDto userPasswordUpdateDto) {

        User user = findEntityByEmail(email);

        if (!passwordEncoder.matches(userPasswordUpdateDto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(userPasswordUpdateDto.getNewPassword()));
    }

    @Transactional
    @Override
    public void deleteProfile(String email) {
        User user = findEntityByEmail(email);

        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByUserId(user.getId());

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByUser(booking);
        }
        user.setDeletedAt(Instant.now());
    }

}
