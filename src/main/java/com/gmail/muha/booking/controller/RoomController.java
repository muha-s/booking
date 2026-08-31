package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.room.RoomAvailableDto;
import com.gmail.muha.booking.dto.room.RoomSearchCriteriaDto;
import com.gmail.muha.booking.service.room.RoomSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomSearchService roomSearchService;

    @GetMapping("/available")
    public List<RoomAvailableDto> findAvailableRooms(
            @Valid @ModelAttribute RoomSearchCriteriaDto roomSearchCriteriaDto) {

        return roomSearchService.findAvailableRooms(roomSearchCriteriaDto);
    }
}