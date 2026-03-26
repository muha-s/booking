package com.gmail.muha.booking;

import com.gmail.muha.booking.config.AppConfig;
import com.gmail.muha.booking.http.HttpServerApp;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        AppConfig config = new AppConfig();
        HttpServerApp.start(config);





    }
}
