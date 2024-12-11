package com.event.management.cli;

import com.event.management.dto.EventDto;
import com.event.management.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

@Component
public class EventCLI {

    @Autowired
    public static final Logger logger = LoggerFactory.getLogger(EventCLI.class);

    private final EventService eventService;

    public EventCLI(EventService eventService) {
        this.eventService = eventService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Event Management CLI!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Event");
            System.out.println("2. List All Events");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    createEvent(scanner);
                    break;
                case 2:
                    listEvents();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void createEvent(Scanner scanner) {
        System.out.println("Enter event details:");

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Organized By: ");
        String organizedBy = scanner.nextLine();

        System.out.print("Event Date (YYYY-MM-DD): ");
        String eventDate = scanner.nextLine();

        System.out.print("Event Time (HH:MM): ");
        String eventTime = scanner.nextLine();

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Ticket Price: ");
        double ticketPrice = scanner.nextDouble();

        System.out.print("Max Ticket Capacity: ");
        int maxCapacity = scanner.nextInt();

        System.out.print("Vendor ID: ");
        Long userID = scanner.nextLong();
        scanner.nextLine(); // Consume the newline

        EventDto eventDto = EventDto.builder()
                .title(title)
                .description(description)
                .organizedBy(organizedBy)
                .eventDate(eventDate)
                .eventTime(eventTime)
                .location(location)
                .ticketPrice(ticketPrice)
                .maxTicketCapacity(maxCapacity)
                .build();

        try {
            eventService.createEvent(eventDto, userID); // Replace null with a userId if required
            System.out.println("Event created successfully!");
            logger.info("Event created successfully!");
        } catch (Exception e) {
            System.out.println("Failed to create event: " + e.getMessage());
        }
    }

    public void listEvents() {
        System.out.println("Listing all events:");
        List<Object[]> events = eventService.findAllEventDetails();
        events.forEach(eventArray -> {
            if (eventArray.length == 12) {  // Adjust length based on number of columns in your query
                Long id = (Long) eventArray[0];
                String title = (String) eventArray[1];
                String optional = (String) eventArray[2];
                String description = (String) eventArray[3];
                String organizedBy = (String) eventArray[4];
                String eventDate = (String) eventArray[5];
                String eventTime = (String) eventArray[6];
                String location = (String) eventArray[7];
                double ticketPrice = (Double) eventArray[8];
                int maxTicketCapacity = (Integer) eventArray[9];
                String image = (String) eventArray[10];
                int likes = (Integer) eventArray[11];

                System.out.println("Event ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Optional: " + optional);
                System.out.println("Description: " + description);
                System.out.println("Organized by: " + organizedBy);
                System.out.println("Event Date: " + eventDate);
                System.out.println("Event Time: " + eventTime);
                System.out.println("Location: " + location);
                System.out.println("Ticket Price: " + ticketPrice);
                System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
                System.out.println("Image: " + image);
                System.out.println("Likes: " + likes);
                System.out.println("----------------------------------------------------");
                logger.info("Event fetch successfully!");
            } else {
                System.out.println("Unexpected data structure");
            }
        });
    }

}
