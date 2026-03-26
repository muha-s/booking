package com.gmail.muha.booking.repository;

import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    Optional<User> update(User user);

    Optional<User> findByEmail(String email);

}
