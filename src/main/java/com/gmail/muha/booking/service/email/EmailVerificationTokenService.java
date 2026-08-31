package com.gmail.muha.booking.service.email;

import com.gmail.muha.booking.model.entity.EmailVerificationToken;
import com.gmail.muha.booking.model.entity.User;

public interface EmailVerificationTokenService {

    EmailVerificationToken create(User user);

    EmailVerificationToken findValidByToken(String token);

    void delete(EmailVerificationToken verificationToken);



}