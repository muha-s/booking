package com.gmail.muha.booking.service.user;

import com.gmail.muha.booking.dto.user.UserCreateDto;
import com.gmail.muha.booking.dto.user.UserEmailUpdateDto;
import com.gmail.muha.booking.dto.user.UserFullDto;

public interface RegistrationService {

    UserFullDto register(UserCreateDto userCreateDto);

    void verifyEmail(String token);

    void updateEmail(String currentEmail, UserEmailUpdateDto userEmailUpdateDto);
}