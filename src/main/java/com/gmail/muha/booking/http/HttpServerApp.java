package com.gmail.muha.booking.http;

import com.gmail.muha.booking.config.AppConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerApp {

    public static void start(AppConfig config) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 1️⃣ Контексты API
        server.createContext("/users", new UserHandler(config.getUserController()));
        server.createContext("/rooms", new RoomHandler(config.getRoomController()));
        server.createContext("/hotels", new HotelHandler(config.getHotelController()));

        // 2️⃣ Контекст для index и root
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();

            // Если root "/" или "/index", показываем index.html
            if ("/".equals(path) || "/index".equals(path)) {
                serveHtml(exchange, "/web/index.html");
            } else {
                // пробуем загрузить любой файл из resources/web
                serveHtml(exchange, "/web" + path);
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8080");
    }

    // Метод для подгрузки HTML/JS/CSS
    private static void serveHtml(com.sun.net.httpserver.HttpExchange exchange, String resourcePath) throws IOException {
        InputStream stream = HttpServerApp.class.getResourceAsStream(resourcePath);

        if (stream == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        byte[] bytes = stream.readAllBytes();

        // Устанавливаем MIME тип (можно расширить для JS/CSS)
        String contentType = "text/html";
        if (resourcePath.endsWith(".js")) contentType = "application/javascript";
        if (resourcePath.endsWith(".css")) contentType = "text/css";

        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}