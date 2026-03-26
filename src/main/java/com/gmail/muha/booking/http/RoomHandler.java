package com.gmail.muha.booking.http;

import com.gmail.muha.booking.controller.RoomController;
import com.gmail.muha.booking.dto.room_dto.RoomResponseDto;
import com.gmail.muha.booking.entity.enums.RoomType;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.RepositoryException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RoomHandler implements HttpHandler {

    private final RoomController controller;

    public RoomHandler(RoomController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            case "DELETE" -> handleDelete(exchange);
            case "PUT" -> handlePut(exchange);
            default -> exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {

        try {
            String path = exchange.getRequestURI().getPath();

            String request = path.substring(path.lastIndexOf('/') + 1);

            String response;

            if ("rooms".equals(request)) {
                response = roomsToJson(controller.getRooms());
            } else {
                response = controller.getRoom(Long.parseLong(request)).toJson();
            }
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid room id");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            byte[] requestBytes = exchange.getRequestBody().readAllBytes();
            String requestBody = new String(requestBytes);

            String[] parts = requestBody.split(",");

            String hotelId = parts[0];
            String roomType = parts[1];
            String price = parts[2];

            String response = controller.createRoom(
                    Long.parseLong(hotelId), RoomType.valueOf(roomType), Double.parseDouble(price)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid room id");

        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "Invalid room type");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        try {
            byte[] requestBytes = exchange.getRequestBody().readAllBytes();
            String requestBody = new String(requestBytes);

            String path = exchange.getRequestURI().getPath();
            String request = path.substring(path.lastIndexOf('/') + 1);
            Long id = Long.parseLong(request);

            String[] parts = requestBody.split(",");

            String roomType = parts[0];
            String price = parts[1];


            String response = controller.updateRoom
                    (id, RoomType.valueOf(roomType), Double.parseDouble(price)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid room id");

        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "Invalid room type");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            String idString = path.substring(path.lastIndexOf('/') + 1);

            Long id = Long.parseLong(idString);

            controller.deleteRoom(id);

            String response = "Room deleted";
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid room id");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private String roomsToJson(List<RoomResponseDto> rooms) {
        StringBuilder response = new StringBuilder("[");

        for (int i = 0; i < rooms.size(); i++) {


            if (i != rooms.size() - 1) {
                response.append(rooms.get(i).toJson()).append(",");
            } else {
                response.append(rooms.get(i).toJson());
            }

        }
        response.append("]");
        return response.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes();

        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}
