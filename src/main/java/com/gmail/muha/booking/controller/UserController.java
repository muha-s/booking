package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.user_dto.UserCreateDto;
import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.dto.user_dto.UserUpdateDto;
import com.gmail.muha.booking.entity.enums.Role;
import com.gmail.muha.booking.service.UserService;

import java.util.List;

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;

    }

    public UserResponseDto createUser(String name, String email, String password, String phone) {
        UserCreateDto createUserDto = new UserCreateDto(name, email, password, phone);
        return service.createUser(createUserDto);

    }

    public UserResponseDto updateUser(Long id, String name, String email, String password, String phone, Role role) {
        UserUpdateDto updateUserDto = new UserUpdateDto(id, name, email, password, phone, role);
        return service.updateUser(updateUserDto);
    }

    public UserResponseDto getUser(Long id) {
        return service.getUser(id);
    }

    public List<UserResponseDto> getUsers() {
        return service.getAllUsers();
    }

    public void deleteUser(Long id) {
        service.deleteUser(id);
    }

    public UserResponseDto login(String email, String password) {
        return service.login(email, password);
    }
}
