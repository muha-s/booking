package com.gmail.muha.booking.service.user.impl;

import com.gmail.muha.booking.dto.user.*;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.UserAlreadyExistsException;
import com.gmail.muha.booking.mapper.UserMapper;
import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.User;
import com.gmail.muha.booking.model.entity.enums.UserRole;
import com.gmail.muha.booking.model.repository.UserRepository;
import com.gmail.muha.booking.service.email.EmailService;
import com.gmail.muha.booking.service.email.EmailVerificationTokenService;
import com.gmail.muha.booking.service.user.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void register(UserCreateDto userCreateDto) {

        if (userRepository.existsByEmailIgnoreCase(userCreateDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User creatingUser = userMapper.toEntity(userCreateDto);

        creatingUser.setPassword(passwordEncoder.encode(creatingUser.getPassword()));

        creatingUser.setEmailVerified(false);

        User savedUser = userRepository.save(creatingUser);

        EmailVerificationToken verificationToken = emailVerificationTokenService.create(savedUser);

        String verificationUrl = "http://localhost:4200/verify-email?token=" + verificationToken.getToken();

        emailService.sendEmail(
                savedUser.getEmail(),
                "Confirm your registration",
                """
                        Thank you for registering.
                        
                        Please confirm your email by following this link:
                        %s
                        """.formatted(verificationUrl));
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {

        EmailVerificationToken verificationToken = emailVerificationTokenService.findValidByToken(token);

        User user = verificationToken.getUser();

        if (user.getPendingEmail() != null) {
            user.setEmail(user.getPendingEmail());
            user.setPendingEmail(null);
        }
        user.setEmailVerified(true);
        emailVerificationTokenService.delete(verificationToken);
    }

    @Override
    @Transactional
    public void updateEmail(String currentEmail, UserEmailUpdateDto userEmailUpdateDto) {

        User user = userRepository.findActiveByEmail(currentEmail).orElseThrow(() ->
                new NotFoundException("User was not found by email: " + currentEmail));

        String newEmail = userEmailUpdateDto.getEmail().trim();

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            throw new UserAlreadyExistsException("New email must be different from current email");
        }

        if (userRepository.existsByEmailIgnoreCase(newEmail) || userRepository.existsByPendingEmailIgnoreCase(newEmail)) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        user.setPendingEmail(newEmail);

        EmailVerificationToken verificationToken = emailVerificationTokenService.create(user);

        String verificationUrl = "http://localhost:4200/verify-email?token=" + verificationToken.getToken();

        emailService.sendEmail(
                newEmail,
                "Confirm your new email",
                """
                        Your email address change has been requested.
                        
                        Please confirm your new email by following this link:
                        
                        %s
                        """.formatted(verificationUrl)
        );
    }

    @Override
    @Transactional
    public void requestRestore(UserRestoreRequestDto userRestoreRequestDto) {

        String email = userRestoreRequestDto.getEmail().trim();

        User user = userRepository.findDeletedByEmail(email)
                .filter(deletedUser -> deletedUser.getRole() == UserRole.USER)
                .orElseThrow(() -> new NotFoundException("Deleted user was not found by email: " + email));

        EmailVerificationToken verificationToken = emailVerificationTokenService.create(user);

        String restoreUrl = "http://localhost:4200/restore-account?token=" + verificationToken.getToken();

        emailService.sendEmail(
                user.getEmail(),
                "Restore your Booking account",
                """
                        A request to restore your Booking account has been received.
                        
                        To restore your account and create a new password, follow this link:
                        %s
                        
                        If you did not request account restoration, ignore this email.
                        """.formatted(restoreUrl)
        );
    }

    @Override
    @Transactional
    public void restore(UserRestoreDto userRestoreDto) {

        EmailVerificationToken verificationToken =
                emailVerificationTokenService.findValidByToken(userRestoreDto.getToken());

        User user = verificationToken.getUser();

        if (user.getRole() != UserRole.USER || user.getDeletedAt() == null) {
            throw new IllegalStateException("Account cannot be restored");
        }

        user.setPassword(passwordEncoder.encode(userRestoreDto.getPassword()));

        user.setDeletedAt(null);

        emailVerificationTokenService.delete(verificationToken);
    }
}
