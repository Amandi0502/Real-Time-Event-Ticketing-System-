package com.event.management.cli;

import com.event.management.entity.TicketEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com.event.management")
public class CliApplication implements CommandLineRunner {

    private final UserCLI userCLI;
    private final EventCLI eventCLI;

    private final TicketCLI ticketCLI;

    public CliApplication(UserCLI userCLI, EventCLI eventCLI , TicketCLI ticketCLI) {
        this.userCLI = userCLI;
        this.eventCLI = eventCLI;
        this.ticketCLI = ticketCLI;
    }

    public static void main(String[] args) {
        // Run the Spring Boot application
        SpringApplication.run(CliApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize UserCLI first
        userCLI.start();

        // Optionally initialize EventCLI after UserCLI
        eventCLI.start();

        ticketCLI.start();
    }
}
