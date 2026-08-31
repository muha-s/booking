package com.gmail.muha.booking.service.user;

import com.gmail.muha.booking.dto.user.*;

public interface RegistrationService {

    void register(UserCreateDto userCreateDto);

    void verifyEmail(String token);

    void updateEmail(String currentEmail, UserEmailUpdateDto userEmailUpdateDto);

    void requestRestore(UserRestoreRequestDto userRestoreRequestDto);

    void restore(UserRestoreDto userRestoreDto);
}