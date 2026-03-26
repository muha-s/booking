package com.gmail.muha.booking.http;

import com.gmail.muha.booking.controller.UserController;
import com.gmail.muha.booking.dto.user_dto.UserResponseDto;
import com.gmail.muha.booking.entity.enums.Role;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.RepositoryException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UserHandler implements HttpHandler {

    private final UserController controller;

    public UserHandler(UserController controller) {
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

            if ("users".equals(request)) {
                response = usersToJson(controller.getUsers());
            } else {
                response = controller.getUser(Long.parseLong(request)).toJson();
            }
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid user id");

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

            String name = parts[0];
            String email = parts[1];
            String password = parts[2];
            String phone = parts[3];

            String response = controller.createUser(name, email, password, phone).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid user id");

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


            String name = parts[0];
            String email = parts[1];
            String password = parts[2];
            String phone = parts[3];
            String role = parts[4];

            String response = controller.updateUser(
                    id, name, email, password, phone, Role.valueOf(role)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid user id");

        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "Invalid role");

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

            controller.deleteUser(id);

            String response = "User deleted";
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid user id");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private String usersToJson(List<UserResponseDto> users) {
        StringBuilder response = new StringBuilder("[");

        for (int i = 0; i < users.size(); i++) {


            if (i != users.size() - 1) {
                response.append(users.get(i).toJson()).append(",");
            } else {
                response.append(users.get(i).toJson());
            }

        }
        response.append("]");
        return response.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}