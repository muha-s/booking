package com.gmail.muha.booking.service.room;

import com.gmail.muha.booking.dto.room.RoomAvailableDto;
import com.gmail.muha.booking.dto.room.RoomSearchCriteriaDto;

import java.util.List;

public interface RoomSearchService {

    List<RoomAvailableDto> findAvailableRooms(RoomSearchCriteriaDto criteria);


}
