package com.gmail.muha.booking.service.impl;


import com.gmail.muha.booking.dto.room_dto.RoomCreateDto;
import com.gmail.muha.booking.dto.room_dto.RoomResponseDto;
import com.gmail.muha.booking.dto.room_dto.RoomUpdateDto;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.entity.Room;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.RoomMapper;
import com.gmail.muha.booking.repository.RoomRepository;
import com.gmail.muha.booking.service.HotelService;
import com.gmail.muha.booking.service.RoomService;

import java.util.ArrayList;
import java.util.List;

public class RoomServiceImpl implements RoomService {

    private final HotelService hotelService;
    private final RoomRepository repository;
    private final RoomMapper mapper;

    public RoomServiceImpl(RoomRepository repository, RoomMapper mapper, HotelService hotelService) {
        this.repository = repository;
        this.mapper = mapper;
        this.hotelService = hotelService;
    }

    @Override
    public RoomResponseDto createRoom(RoomCreateDto dto) {
        hotelService.findHotelEntityById(dto.getHotelId());
        Room room = mapper.toEntity(dto);

        room = repository.save(room);
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());
        return mapper.toResponseDto(room, hotel);
    }

    @Override
    public RoomResponseDto getRoom(Long id) {
        Room room = findRoomEntityById(id);
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());
        return mapper.toResponseDto(room, hotel);
    }

    @Override
    public List<RoomResponseDto> getAllRooms() {
        return toDtoList(repository.findAll());
    }

    @Override
    public RoomResponseDto updateRoom(RoomUpdateDto roomUpdateDto) {
        Room room = findRoomEntityById(roomUpdateDto.getId());
        room.setRoomType(roomUpdateDto.getRoomType());
        room.setPrice(roomUpdateDto.getPrice());

        room = repository.update(room);
        Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());
        return mapper.toResponseDto(room, hotel);
    }

    @Override
    public void deleteRoom(Long id) {
        findRoomEntityById(id);
        repository.deleteById(id);
    }

    @Override
    public Room findRoomEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Room not found with id: " + id));
    }

    private List<RoomResponseDto> toDtoList(List<Room> rooms) {

        List<RoomResponseDto> roomDtoList = new ArrayList<>();

        for (Room room : rooms) {

            Hotel hotel = hotelService.findHotelEntityById(room.getHotelId());

            roomDtoList.add(mapper.toResponseDto(room, hotel));
        }
        return roomDtoList;
    }
}
