package com.gmail.muha.booking.service.room.impl;

import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.RoomMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.RoomRepository;
import com.gmail.muha.booking.service.booking.BookingCancellationService;
import com.gmail.muha.booking.service.room.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingCancellationService bookingCancellationService;
    private final RoomMapper roomMapper;

    @Override
    public RoomManagedDto create(RoomManagedCreateDto roomManagedCreateDto, Hotel hotel) {

        Room room = roomMapper.toEntity(roomManagedCreateDto, hotel);
        Room savedRoom = roomRepository.save(room);

        return roomMapper.toManagedDto(savedRoom);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        List<Booking> bookings = bookingRepository.findFutureActiveBookingsByRoomId(id);

        for (Booking booking : bookings) {
            bookingCancellationService.cancelByAdministration(booking);
        }

        Room room = findEntityById(id);
        room.setDeletedAt(Instant.now());
    }

    private Room findEntityById(Long id) {
        return roomRepository.findActiveById(id)
                .orElseThrow(() ->
                        new NotFoundException("Room was not found by id: " + id));
    }
}