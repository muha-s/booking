package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final ShortDtoMapper shortDtoMapper;

    public UserMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
    }

    public UserFullDto toFullDto(User user) {

        UserFullDto userFullDto = new UserFullDto();

        userFullDto.setId(user.getId());
        userFullDto.setRole(user.getRole());
        userFullDto.setFirstName(user.getFirstName());
        userFullDto.setLastName(user.getLastName());
        userFullDto.setPhone(user.getPhone());
        userFullDto.setEmail(user.getEmail());
        userFullDto.setBalance(user.getBalance());
        userFullDto.setBookings(shortDtoMapper.toBookingShortDtoList(user.getBookings()));
        userFullDto.setManagedHotels(shortDtoMapper.toHotelShortDtoSet(user.getManagedHotels()));

        return userFullDto;
    }

    public UserProfileDto toUserProfileDto(User user) {

        UserProfileDto userProfileDto = new UserProfileDto();

        userProfileDto.setId(user.getId());
        userProfileDto.setRole(user.getRole());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setPhone(user.getPhone());
        userProfileDto.setBalance(user.getBalance());

        return userProfileDto;
    }

    public UserShortDto toShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();

        userShortDto.setId(user.getId());
        userShortDto.setRole(user.getRole());
        userShortDto.setFirstName(user.getFirstName());
        userShortDto.setLastName(user.getLastName());
        userShortDto.setPhone(user.getPhone());
        return userShortDto;
    }

    public User toEntity(UserCreateDto userCreateDto) {
        User user = new User();

        user.setRole(UserRole.USER);
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPhone(userCreateDto.getPhone());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setBalance(userCreateDto.getInitialBalance());

        return user;
    }

    public void updateEntity(UserUpdateDto userUpdateDto, User entity) {

        if (userUpdateDto.getFirstName() != null) {
            entity.setFirstName(userUpdateDto.getFirstName());
        }

        if (userUpdateDto.getLastName() != null) {
            entity.setLastName(userUpdateDto.getLastName());
        }

        if (userUpdateDto.getPhone() != null) {
            entity.setPhone(userUpdateDto.getPhone());
        }

    }

    public List<UserShortDto> toShortDtoList(List<User> users) {
        return users.stream()
                .map(this::toShortDto)
                .toList();
    }
}
