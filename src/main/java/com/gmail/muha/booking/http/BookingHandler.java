package com.gmail.muha.booking.http;

import com.gmail.muha.booking.controller.BookingController;
import com.gmail.muha.booking.dto.booking_dto.BookingResponseDto;
import com.gmail.muha.booking.exception.NotFoundException;
import com.gmail.muha.booking.exception.RepositoryException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;

public class BookingHandler implements HttpHandler {

    private final BookingController controller;

    public BookingHandler(BookingController controller) {
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

            if ("bookings".equals(request)) {
                response = bookingsToJson(controller.getBookings());
            } else {
                response = controller.getBooking(Long.parseLong(request)).toJson();
            }
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid booking id");

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


            String userId = parts[0];
            String roomId = parts[1];
            String startDate = parts[2];
            String endDate = parts[3];

            String response = controller.createBooking(Long.parseLong(userId), Long.parseLong(roomId),
                    Date.valueOf(startDate), Date.valueOf(endDate)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid booking id");

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

            String roomId = parts[0];
            String startDate = parts[1];
            String endDate = parts[2];

            String response = controller.updateBooking(
                    id, Long.parseLong(roomId), Date.valueOf(startDate), Date.valueOf(endDate)).toJson();

            sendResponse(exchange, 200, response);
        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid booking id");

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

            controller.deleteBooking(id);

            String response = "Booking deleted";
            sendResponse(exchange, 200, response);

        } catch (NotFoundException e) {
            sendResponse(exchange, 404, e.getMessage());

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Invalid booking id");

        } catch (RepositoryException e) {
            sendResponse(exchange, 500, "Database error");

        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal server error");
        }
    }

    private String bookingsToJson(List<BookingResponseDto> bookings) {
        StringBuilder response = new StringBuilder("[");

        for (int i = 0; i < bookings.size(); i++) {


            if (i != bookings.size() - 1) {
                response.append(bookings.get(i).toJson()).append(",");
            } else {
                response.append(bookings.get(i).toJson());
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
