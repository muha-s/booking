package com.gmail.muha.booking.http;

import com.gmail.muha.booking.controller.HotelController;
import com.gmail.muha.booking.dto.hotel_dto.HotelResponseDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.RepositoryException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HotelHandler implements HttpHandler {

    private final HotelController controller;

    public HotelHandler(HotelController controller) {
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

            if ("hotels".equals(request)) {
                response = hotelsToJson(controller.getHotels());
            } else {
                response = controller.getHotel(Long.parseLong(request)).toJson();
            }
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid hotel id");

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
            String cityId = parts[1];

            String response = controller.createHotel(name, Long.parseLong(cityId)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid city id");

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

            String response = controller.updateHotel(
                    id, name).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid hotel id");

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

            controller.deleteHotel(id);

            String response = "Hotel deleted";
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid hotel id");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private String hotelsToJson(List<HotelResponseDto> hotels) {
        StringBuilder response = new StringBuilder("[");

        for (int i = 0; i < hotels.size(); i++) {

            if (i != hotels.size() - 1) {
                response.append(hotels.get(i).toJson()).append(",");
            } else {
                response.append(hotels.get(i).toJson());
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
