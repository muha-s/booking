package com.gmail.muha.booking.repository.impl;

import com.gmail.muha.booking.config.DatabaseConfig;
import com.gmail.muha.booking.entity.User;
import com.gmail.muha.booking.entity.enums.Role;
import com.gmail.muha.booking.exception.RepositoryException;
import com.gmail.muha.booking.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private UserRepositoryImpl() {
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users(name, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole().name());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }

            return user;

        } catch (SQLException e) {
            throw new RepositoryException("Error saving user", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = mapRowToUser(resultSet);
                return Optional.of(user);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding user by id", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }

            return users;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding all users", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting user", e);
        }
    }

    @Override
    public Optional<User> update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, phone = ?, role = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole().name());
            statement.setLong(6, user.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty();
            }
            return Optional.of(user);

        } catch (SQLException e) {
            throw new RepositoryException("Error updating user", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = mapRowToUser(resultSet);
                return Optional.of(user);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding user by email", e);
        }
    }


    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setPhone(resultSet.getString("phone"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
    }

    private static class Holder {
        private static final UserRepository INSTANCE = new UserRepositoryImpl();
    }

    public static UserRepository getInstance() {
        return Holder.INSTANCE;
    }
}