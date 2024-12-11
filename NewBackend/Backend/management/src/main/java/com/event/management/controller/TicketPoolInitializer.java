package com.event.management.controller;

import com.event.management.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TicketPoolInitializer {

    @Autowired
    private TicketService ticketService;

    @PostConstruct
    public void setup() {
        // Initialize ticket pool after the application starts
        ticketService.initializeTicketPool();
        System.out.println("Ticket pool initialized.");
    }
}
