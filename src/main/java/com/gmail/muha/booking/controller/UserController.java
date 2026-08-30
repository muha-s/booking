package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.service.user.RegistrationService;
import com.gmail.muha.booking.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegistrationService registrationService;


    @GetMapping
    public List<UserShortDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserFullDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserFullDto create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return registrationService.register(userCreateDto);
    }

    @PutMapping("/{id}")
    public UserFullDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(id, userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/me")
    public UserProfileDto findProfile(Authentication authentication) {
        return userService.findProfileByEmail(authentication.getName());
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@RequestParam String token) {
        registrationService.verifyEmail(token);
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
