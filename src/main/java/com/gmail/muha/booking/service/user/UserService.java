package com.gmail.muha.booking.service.user;

import com.gmail.muha.booking.dto.user.UserPasswordUpdateDto;
import com.gmail.muha.booking.dto.user.UserProfileDto;
import com.gmail.muha.booking.dto.user.UserUpdateDto;
import com.gmail.muha.booking.model.entity.User;

public interface UserService {

    UserProfileDto findProfileByEmail(String email);

    User findEntityByEmail(String email);

    UserProfileDto updateProfile(String email, UserUpdateDto userUpdateDto);

    void updatePassword(String email, UserPasswordUpdateDto userPasswordUpdateDto);

    void deleteProfile(String email);
}
