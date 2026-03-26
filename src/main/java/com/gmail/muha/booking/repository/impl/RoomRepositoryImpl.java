package com.gmail.muha.booking.repository.impl;

import com.gmail.muha.booking.config.DatabaseConfig;
import com.gmail.muha.booking.entity.Room;
import com.gmail.muha.booking.entity.enums.RoomType;
import com.gmail.muha.booking.exception.RepositoryException;
import com.gmail.muha.booking.repository.RoomRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepository {


    private RoomRepositoryImpl() {

    }

    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO rooms( hotel_id, room_type, price) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, room.getHotelId());
            statement.setString(2, room.getRoomType().name());
            statement.setDouble(3, room.getPrice());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                room.setId(generatedKeys.getLong(1));
            }
            return room;

        } catch (SQLException e) {
            throw new RepositoryException("Error saving room", e);
        }
    }

    @Override
    public Optional<Room> findById(Long id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Room room = mapRowToRoom(resultSet);
                return Optional.of(room);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding room by id", e);
        }
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT * FROM rooms";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<Room> rooms = new ArrayList<>();

            while (resultSet.next()) {
                rooms.add(mapRowToRoom(resultSet));
            }
            return rooms;

        } catch (SQLException e) {
            throw new RepositoryException("Error finding all rooms", e);
        }
    }

    @Override
    public Room update(Room room) {
        String sql = "UPDATE rooms SET room_type = ?, price = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, String.valueOf(room.getRoomType()));
            statement.setDouble(2, room.getPrice());

            statement.setLong(3, room.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("No room found with id: " + room.getId());
            }
            return room;

        } catch (SQLException e) {
            throw new RepositoryException("Error updating room", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM rooms WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting room", e);
        }
    }

    private Room mapRowToRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getLong("id"));
        room.setHotelId(resultSet.getLong("hotel_id"));
        room.setRoomType(RoomType.valueOf(resultSet.getString("room_type")));
        room.setPrice(resultSet.getDouble("price"));

        return room;
    }

    private static class Holder {
        private static final RoomRepository INSTANCE = new RoomRepositoryImpl();
    }

    public static RoomRepository getInstance() {
        return Holder.INSTANCE;
    }
}
