package com.gmail.muha.booking.service.impl;

import com.gmail.muha.booking.dto.user_dto.UserCreateDto;
import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.dto.user_dto.UserUpdateDto;
import com.gmail.muha.booking.entity.User;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.mapper.UserMapper;
import com.gmail.muha.booking.repository.UserRepository;
import com.gmail.muha.booking.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponseDto createUser(UserCreateDto dto) {
        User user = mapper.toEntity(dto);

        user = repository.save(user);
        return mapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = findUserEntityById(id);
        return mapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public UserResponseDto updateUser(UserUpdateDto userUpdate) {
        User user = findUserEntityById(userUpdate.getId());
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(userUpdate.getPassword());
        user.setPhone(userUpdate.getPhone());
        user.setRole(userUpdate.getRole());
        repository.update(user);
        return mapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password");
        }
        return mapper.toResponseDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        findUserEntityById(id);
        repository.deleteById(id);
    }

    @Override
    public User findUserEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }


}
