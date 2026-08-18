package com.gmail.muha.booking.service;

import com.gmail.muha.booking.dto.user.UserCreateDto;
import com.gmail.muha.booking.dto.user.UserFullDto;
import com.gmail.muha.booking.dto.user.UserShortDto;
import com.gmail.muha.booking.dto.user.UserUpdateDto;
import com.gmail.muha.booking.model.entity.User;

import java.util.List;

public interface UserService {

    UserFullDto findById(Long id);

    User findEntityById(Long id);

    List<UserShortDto> findAll();

    UserFullDto create(UserCreateDto userCreateDto);

    UserFullDto update(Long id, UserUpdateDto userUpdateDto);

    void deleteById(Long id);
}
