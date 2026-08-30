package com.gmail.muha.booking.service.user;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.model.entity.User;

import java.util.List;

public interface UserService {

    UserFullDto findById(Long id);

    User findEntityById(Long id);

    List<UserShortDto> findAll();

    UserFullDto update(Long id, UserUpdateDto userUpdateDto);

    void deleteById(Long id);

    UserProfileDto findProfileByEmail(String email);

    User findEntityByEmail(String email);

    UserProfileDto updateProfile(String email, UserUpdateDto userUpdateDto);

    void updatePassword(String email, UserPasswordUpdateDto userPasswordUpdateDto);

    void deleteProfile(String email);
}
