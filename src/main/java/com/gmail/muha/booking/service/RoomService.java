package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.room_dto.RoomCreateDto;
import com.gmail.muha.booking.dto.room_dto.RoomResponseDto;
import com.gmail.muha.booking.dto.room_dto.RoomUpdateDto;
import com.gmail.muha.booking.entity.Room;

import java.util.List;

public interface RoomService {

    RoomResponseDto createRoom(RoomCreateDto dto);

    RoomResponseDto getRoom(Long id);

    List<RoomResponseDto> getAllRooms();

    void deleteRoom(Long id);

    Room findRoomEntityById(Long id);

    RoomResponseDto updateRoom(RoomUpdateDto roomUpdateDto);

}
