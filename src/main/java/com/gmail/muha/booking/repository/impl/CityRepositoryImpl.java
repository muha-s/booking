package com.gmail.muha.booking.repository.impl;

import com.gmail.muha.booking.config.DatabaseConfig;
import com.gmail.muha.booking.entity.City;
import com.gmail.muha.booking.exception.RepositoryException;
import com.gmail.muha.booking.repository.CityRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityRepositoryImpl implements CityRepository {

    private CityRepositoryImpl() {

    }

    @Override
    public City save(City city) {
        String sql = "INSERT INTO cities(name) VALUES (?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, city.getName());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                city.setId(generatedKeys.getLong(1));
            }

            return city;

        } catch (SQLException e) {
            throw new RepositoryException("Error saving city", e);
        }
    }

    @Override
    public Optional<City> findById(Long id) {
        String sql = "SELECT * FROM cities WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                City city = mapRowToCity(resultSet);
                return Optional.of(city);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding city by id", e);
        }
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT * FROM cities";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<City> cities = new ArrayList<>();

            while (resultSet.next()) {
                cities.add(mapRowToCity(resultSet));
            }

            return cities;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding all cities", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cities WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting city", e);
        }
    }

    private City mapRowToCity(ResultSet resultSet) throws SQLException {
        City city = new City();
        city.setId(resultSet.getLong("id"));
        city.setName(resultSet.getString("name"));

        return city;
    }

    private static class Holder {
        private static final CityRepository INSTANCE = new CityRepositoryImpl();
    }

    public static CityRepository getInstance() {
        return Holder.INSTANCE;
    }
}
