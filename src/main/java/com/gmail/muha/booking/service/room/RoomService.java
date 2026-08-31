package com.gmail.muha.booking.service.room;

import com.gmail.muha.booking.dto.room.RoomManagedCreateDto;
import com.gmail.muha.booking.dto.room.RoomManagedDto;
import com.gmail.muha.booking.model.entity.Hotel;

public interface RoomService {

    RoomManagedDto create(RoomManagedCreateDto roomManagedCreateDto, Hotel hotel);

    void deleteById(Long id);
}