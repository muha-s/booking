package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.room.*;
import com.gmail.muha.booking.model.entity.Room;

import java.util.List;

public interface RoomService {

    RoomFullDto findById(Long id);

    Room findEntityById(Long id);

    List<RoomShortDto> findAll();

    RoomFullDto create(RoomCreateDto roomCreateDto);

    List<RoomShortDto> createRooms(RoomBatchCreateDto roomBatchCreateDto);

    RoomFullDto update(Long id, RoomUpdateDto roomUpdateDto);

    void deleteById(Long id);
}
