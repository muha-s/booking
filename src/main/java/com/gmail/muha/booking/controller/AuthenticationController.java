package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.dto.auth.AuthInfoDto;
import com.gmail.muha.booking.dto.auth.LoginRequestDto;
import com.gmail.muha.booking.dto.auth.LoginResponseDto;
import com.gmail.muha.booking.exception.AuthenticationRoleException;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import com.gmail.muha.booking.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword()));

        String token = jwtService.generateToken(authentication);
        UserRole role = getRole(authentication);

        return new LoginResponseDto(token, role);
    }

    @GetMapping("/me")
    public AuthInfoDto getCurrentAuthentication(Authentication authentication) {
        return new AuthInfoDto(getRole(authentication));
    }

    private UserRole getRole(Authentication authentication) {

        String authority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(value -> value.startsWith("ROLE_"))
                .findFirst()
                .orElseThrow(() ->
                        new AuthenticationRoleException("Authenticated user has no role"));

        return UserRole.valueOf(authority.replace("ROLE_", ""));
    }
}