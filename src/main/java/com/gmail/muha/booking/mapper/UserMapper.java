package com.gmail.muha.booking.mapper;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class UserMapper {

    private final ShortDtoMapper shortDtoMapper;

    public UserMapper(ShortDtoMapper shortDtoMapper) {
        this.shortDtoMapper = shortDtoMapper;
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

    public UserSummaryDto toUserSummaryDto(User user) {
        UserSummaryDto userSummaryDto = new UserSummaryDto();

        userSummaryDto.setId(user.getId());
        userSummaryDto.setRole(user.getRole());
        userSummaryDto.setFirstName(user.getFirstName());
        userSummaryDto.setLastName(user.getLastName());
        userSummaryDto.setPhone(user.getPhone());
        userSummaryDto.setEmail(user.getEmail());
        userSummaryDto.setDeletedAt(user.getDeletedAt());
        return userSummaryDto;
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

    public User toEntity(HotelAdminCreateDto hotelAdminCreateDto) {
        User user = new User();

        user.setRole(UserRole.HOTEL_ADMIN);
        user.setFirstName(hotelAdminCreateDto.getFirstName());
        user.setLastName(hotelAdminCreateDto.getLastName());
        user.setPhone(hotelAdminCreateDto.getPhone());
        user.setEmail(hotelAdminCreateDto.getEmail());
        user.setBalance(BigDecimal.ZERO);

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

    public List<UserSummaryDto> toUserSummaryDtoList(List<User> users) {
        return users.stream()
                .map(this::toUserSummaryDto)
                .toList();
    }

    public HotelAdminDto toHotelAdminDto(User user) {
        HotelAdminDto hotelAdminDto = new HotelAdminDto();

        hotelAdminDto.setId(user.getId());
        hotelAdminDto.setFirstName(user.getFirstName());
        hotelAdminDto.setLastName(user.getLastName());
        hotelAdminDto.setPhone(user.getPhone());
        hotelAdminDto.setEmail(user.getEmail());
        hotelAdminDto.setEmailVerified(user.isEmailVerified());
        hotelAdminDto.setManagedHotels(shortDtoMapper.toHotelShortDtoSet(user.getManagedHotels()));

        return hotelAdminDto;
    }
}
