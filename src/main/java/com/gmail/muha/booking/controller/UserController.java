package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.user.UserCreateDto;
import com.gmail.muha.booking.dto.user.UserFullDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.dto.user.UserUpdateDto;
import com.gmail.muha.booking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public List<UserShortDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserFullDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserFullDto create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }

    @PutMapping("/{id}")
    public UserFullDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
