package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.room_dto.RoomCreateDto;
import com.gmail.muha.booking.dto.room_dto.RoomResponseDto;
import com.gmail.muha.booking.dto.room_dto.RoomUpdateDto;
import com.gmail.muha.booking.entity.enums.RoomType;
import com.gmail.muha.booking.service.RoomService;

import java.util.List;

public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    public RoomResponseDto createRoom(Long hotelId, RoomType roomType, double price) {
        RoomCreateDto createRoomDto = new RoomCreateDto(hotelId, roomType, price);
        return service.createRoom(createRoomDto);
    }

    public RoomResponseDto getRoom(Long id) {
        return service.getRoom(id);
    }

    public List<RoomResponseDto> getRooms() {
        return service.getAllRooms();
    }

    public RoomResponseDto updateRoom(Long id, RoomType roomType, double price) {
        RoomUpdateDto roomUpdateDto = new RoomUpdateDto(id, roomType, price);

        return service.updateRoom(roomUpdateDto);
    }

    public void deleteRoom(Long id) {
        service.deleteRoom(id);
    }
}
