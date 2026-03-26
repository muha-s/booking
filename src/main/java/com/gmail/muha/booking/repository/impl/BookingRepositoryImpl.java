package com.gmail.muha.booking.repository.impl;

import com.gmail.muha.booking.config.DatabaseConfig;
import com.gmail.muha.booking.entity.Booking;
import com.gmail.muha.booking.exception.RepositoryException;
import com.gmail.muha.booking.repository.BookingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepositoryImpl implements BookingRepository {


    private BookingRepositoryImpl() {
    }

    @Override
    public Booking save(Booking booking) {
        String sql = "INSERT INTO bookings(user_id, room_id, start_date, end_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, booking.getUserId());
            statement.setLong(2, booking.getRoomId());
            statement.setDate(3, booking.getStartDate());
            statement.setDate(4, booking.getEndDate());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                booking.setId(generatedKeys.getLong(1));
            }

            return booking;

        } catch (SQLException e) {
            throw new RepositoryException("Error saving booking", e);
        }
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Booking booking = mapRowToBooking(resultSet);
                return Optional.of(booking);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding booking by id", e);
        }
    }

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM bookings";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                bookings.add(mapRowToBooking(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding all bookings", e);
        }
    }

    @Override
    public Booking update(Booking booking) {
        String sql = "UPDATE bookings SET room_id = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, booking.getRoomId());
            statement.setDate(2, booking.getStartDate());
            statement.setDate(3, booking.getEndDate());

            statement.setLong(4, booking.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("No booking found with id: " + booking.getId());
            }
            return booking;

        } catch (SQLException e) {
            throw new RepositoryException("Error updating booking", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting booking", e);
        }
    }

    @Override
    public List<Booking> findByUserId(Long userId) {

        String sql = "SELECT * FROM bookings WHERE user_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                bookings.add(mapRowToBooking(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding booking by user_id", e);
        }
    }

    @Override
    public List<Booking> findByRoomId(Long roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (resultSet.next()) {
                bookings.add(mapRowToBooking(resultSet));
            }

            return bookings;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding booking by room_id", e);
        }
    }

    private Booking mapRowToBooking(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getLong("id"));
        booking.setUserId(resultSet.getLong("user_id"));
        booking.setRoomId(resultSet.getLong("room_id"));
        booking.setStartDate(resultSet.getDate("start_date"));
        booking.setEndDate(resultSet.getDate("end_date"));

        return booking;
    }


    private static class Holder {
        private static final BookingRepository INSTANCE = new BookingRepositoryImpl();
    }

    public static BookingRepository getInstance() {
        return Holder.INSTANCE;
    }


}
