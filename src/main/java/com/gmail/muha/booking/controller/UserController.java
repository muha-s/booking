package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.service.user.RegistrationService;
import com.gmail.muha.booking.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegistrationService registrationService;

    @PostMapping
    public void create(@Valid @RequestBody UserCreateDto userCreateDto) {
        registrationService.register(userCreateDto);
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@RequestParam String token) {
        registrationService.verifyEmail(token);
    }

    @PostMapping("/restore-request")
    public void requestRestore(@Valid @RequestBody UserRestoreRequestDto userRestoreRequestDto) {
        registrationService.requestRestore(userRestoreRequestDto);
    }

    @PostMapping("/restore")
    public void restore(@Valid @RequestBody UserRestoreDto userRestoreDto) {
        registrationService.restore(userRestoreDto);
    }

    @GetMapping("/me")
    public UserProfileDto findProfile(Authentication authentication) {
        return userService.findProfileByEmail(authentication.getName());
    }

    @PutMapping("/me")
    public UserProfileDto updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {

        return userService.updateProfile(authentication.getName(), userUpdateDto);
    }

    @PutMapping("/me/password")
    public void updatePassword(
            @Valid @RequestBody UserPasswordUpdateDto userPasswordUpdateDto, Authentication authentication) {
        userService.updatePassword(authentication.getName(), userPasswordUpdateDto);
    }

    @PutMapping("/me/email")
    public void updateEmail(@Valid @RequestBody UserEmailUpdateDto userEmailUpdateDto, Authentication authentication) {

        registrationService.updateEmail(authentication.getName(), userEmailUpdateDto);
    }

    @DeleteMapping("/me")
    public void deleteProfile(Authentication authentication) {
        userService.deleteProfile(authentication.getName());
    }
}