package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.room.*;
import com.gmail.muha.booking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<RoomShortDto> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public RoomFullDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    public RoomFullDto create(@Valid @RequestBody RoomCreateDto roomCreateDto) {
        return roomService.create(roomCreateDto);
    }

    @PostMapping("/batch")
    public List<RoomShortDto> createRooms(@Valid @RequestBody RoomBatchCreateDto roomBatchCreateDto) {
        return roomService.createRooms(roomBatchCreateDto);
    }

    @PutMapping("/{id}")
    public RoomFullDto update(@PathVariable Long id, @Valid @RequestBody RoomUpdateDto roomUpdateDto) {
        return roomService.update(id, roomUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        roomService.deleteById(id);
    }
}
