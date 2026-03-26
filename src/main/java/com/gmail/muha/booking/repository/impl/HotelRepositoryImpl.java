package com.gmail.muha.booking.repository.impl;

import com.gmail.muha.booking.config.DatabaseConfig;
import com.gmail.muha.booking.entity.Hotel;
import com.gmail.muha.booking.exception.RepositoryException;
import com.gmail.muha.booking.repository.HotelRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelRepositoryImpl implements HotelRepository {

    private HotelRepositoryImpl() {

    }

    @Override
    public Hotel save(Hotel hotel) {
        String sql = "INSERT INTO hotels(name, city_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, hotel.getName());
            statement.setLong(2, hotel.getCityId());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                hotel.setId(generatedKeys.getLong(1));
            }

            return hotel;

        } catch (SQLException e) {
            throw new RepositoryException("Error saving hotel", e);
        }
    }

    @Override
    public Optional<Hotel> findById(Long id) {
        String sql = "SELECT * FROM hotels WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Hotel hotel = mapRowToHotel(resultSet);
                return Optional.of(hotel);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding hotel by id", e);
        }
    }

    @Override
    public List<Hotel> findAll() {
        String sql = "SELECT * FROM hotels";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<Hotel> hotels = new ArrayList<>();

            while (resultSet.next()) {
                hotels.add(mapRowToHotel(resultSet));
            }

            return hotels;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding all hotels", e);
        }
    }

    @Override
    public Hotel update(Hotel hotel) {
        String sql = "UPDATE hotels SET name = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hotel.getName());
            statement.setLong(2, hotel.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("No hotel found with id: " + hotel.getId());
            }
            return hotel;

        } catch (SQLException e) {
            throw new RepositoryException("Error updating hotel", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM hotels WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting hotel", e);
        }
    }

    private Hotel mapRowToHotel(ResultSet resultSet) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(resultSet.getLong("id"));
        hotel.setName(resultSet.getString("name"));
        hotel.setCityId(resultSet.getLong("city_id"));

        return hotel;
    }

    private static class Holder {
        private static final HotelRepository INSTANCE = new HotelRepositoryImpl();
    }

    public static HotelRepository getInstance() {
        return Holder.INSTANCE;
    }
}
