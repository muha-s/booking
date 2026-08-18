package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.user.UserCreateDto;
import com.gmail.muha.booking.dto.user.UserFullDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.dto.user.UserUpdateDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.UserMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.UserRepository;
import com.gmail.muha.booking.service.BookingCancellationService;
import com.gmail.muha.booking.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
        return userRepository.findActiveById(id)
                .orElseThrow(() ->
                        new NotFoundException("User was not found by id: " + id));
    }

    @Override
    public List<UserShortDto> findAll() {
        return userMapper.toShortDtoList(userRepository.findAllActive());
    }

    @Override
    public UserFullDto create(UserCreateDto userCreateDto) {

        User creatingUser = userMapper.toEntity(userCreateDto);
        creatingUser.setPassword(passwordEncoder.encode(creatingUser.getPassword()));
        User savedUser = userRepository.save(creatingUser);
        return userMapper.toFullDto(savedUser);
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
}
