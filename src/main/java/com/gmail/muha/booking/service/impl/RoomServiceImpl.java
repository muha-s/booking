package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.room.*;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.RoomMapper;
import com.gmail.muha.booking.model.entity.Booking;
import com.gmail.muha.booking.model.entity.Hotel;
import com.gmail.muha.booking.model.entity.Room;
import com.gmail.muha.booking.model.repository.BookingRepository;
import com.gmail.muha.booking.model.repository.RoomRepository;
import com.gmail.muha.booking.service.BookingCancellationService;
import com.gmail.muha.booking.service.HotelService;
import com.gmail.muha.booking.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomMapper roomMapper;
    private final HotelService hotelService;
    private final BookingCancellationService bookingCancellationService;


    @Override
    public RoomFullDto findById(Long id) {
        return roomMapper.toFullDto(findEntityById(id));
    }

    @Override
    public Room findEntityById(Long id) {
        return roomRepository.findActiveById(id)
                .orElseThrow(() ->
                        new NotFoundException("Room was not found by id: " + id));
    }

    @Override
    public List<RoomShortDto> findAll() {
        return roomMapper.toShortDtoList(roomRepository.findAllActive());
    }

    @Override
    public RoomFullDto create(RoomCreateDto roomCreateDto) {

        Room savedRoom = roomRepository.save(saveRoom(roomCreateDto));
        return roomMapper.toFullDto(savedRoom);
    }

    @Transactional
    @Override
    public List<RoomShortDto> createRooms(RoomBatchCreateDto roomBatchCreateDto) {

        RoomCreateDto roomCreateDto = roomMapper.toCreateDto(roomBatchCreateDto);
        List<RoomShortDto> roomShortDtoList = new ArrayList<>();
        for (int i = 0; i < roomBatchCreateDto.getQuantity(); i++) {

            roomShortDtoList.add(roomMapper.toShortDto(saveRoom(roomCreateDto)));
        }
        return roomShortDtoList;
    }

    @Override
    public RoomFullDto update(Long id, RoomUpdateDto roomUpdateDto) {

        Room updatingRoom = findEntityById(id);
        roomMapper.updateEntity(roomUpdateDto, updatingRoom);
        return roomMapper.toFullDto(roomRepository.save(updatingRoom));
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

    private Room saveRoom(RoomCreateDto roomCreateDto) {
        Hotel hotel = hotelService.findEntityById(roomCreateDto.getHotelId());
        return roomRepository.save(roomMapper.toEntity(roomCreateDto, hotel));
    }

}
