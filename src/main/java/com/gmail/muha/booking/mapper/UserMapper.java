package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.user_dto.UserCreateDto;
import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.dto.user_dto.UserUpdateDto;
import com.gmail.muha.booking.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    private UserMapper() {
    }

    public User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getPhone());
    }

    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole());
    }

    public UserUpdateDto toUpdateDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserUpdateDto(
                user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), user.getRole());
    }

    public List<UserResponseDto> toDtoList(List<User> users) {

        List<UserResponseDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            userDtoList.add(toResponseDto(user));
        }
        return userDtoList;
    }

    private static class Holder {
        private static final UserMapper INSTANCE = new UserMapper();
    }

    public static UserMapper getInstance() {
        return Holder.INSTANCE;
    }
}
