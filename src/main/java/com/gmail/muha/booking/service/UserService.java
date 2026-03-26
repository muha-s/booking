package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.user_dto.UserCreateDto;
import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.dto.user_dto.UserUpdateDto;
import com.gmail.muha.booking.entity.User;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserCreateDto dto);

    UserResponseDto getUser(Long id);

    List<UserResponseDto> getAllUsers();

    void deleteUser(Long id);

    User findUserEntityById(Long id);

    UserResponseDto updateUser(UserUpdateDto userUpdateDto);

    UserResponseDto login(String email, String password);

}
