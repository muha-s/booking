package com.gmail.muha.booking.controller;

import com.gmail.muha.booking.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping
    public String sendTestEmail() {
        emailService.sendTestEmail();
        return "Не забывай убираться в квартире!!!!!!!";
    }
}