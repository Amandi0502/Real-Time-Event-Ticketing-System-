package com.event.management.cli;

import com.event.management.dto.TicketPurchaseDto;
import com.event.management.entity.TicketEntity;
import com.event.management.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class TicketCLI {

    private final TicketService ticketService;

    @Autowired
    public TicketCLI(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Define the 'start' method
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("------ Ticket Management CLI ------");
            System.out.println("1. Purchase Ticket");
            System.out.println("2. View Tickets");
            System.out.println("3. Delete Ticket");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    purchaseTicket(scanner);
                    break;
                case 2:
                    viewTickets(scanner);
                    break;
                case 3:
                    deleteTicket(scanner);
                    break;
                case 4:
                    System.out.println("Exiting Ticket Management CLI.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void purchaseTicket(Scanner scanner) {
        try {
            TicketPurchaseDto ticketDto = new TicketPurchaseDto();

            // Collect the ticket details from the user
            System.out.print("Enter event ID: ");
            ticketDto.setEventId(scanner.nextLong());
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter name: ");
            ticketDto.setName(scanner.nextLine());

            System.out.print("Enter contact number: ");
            ticketDto.setContactNo(scanner.nextLine());

            System.out.print("Enter email: ");
            ticketDto.setEmail(scanner.nextLine());

            System.out.print("Enter quantity: ");
            ticketDto.setQuantity(scanner.nextInt());

            System.out.print("Enter total price: ");
            ticketDto.setTotalPrice(scanner.nextDouble());

            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter event date (YYYY-MM-DD): ");
            ticketDto.setEventDate(scanner.nextLine());

            System.out.print("Enter event time (HH:MM): ");
            ticketDto.setEventTime(scanner.nextLine());

            System.out.print("Enter event name: ");
            ticketDto.setEventName(scanner.nextLine());

            System.out.print("Customer ID: ");
            Long userID = scanner.nextLong();

            // Purchase the ticket
            ticketService.purchaseTicketNew(ticketDto, userID); // Assuming user ID is 1 for this example
            System.out.println("Ticket purchased successfully!");

        } catch (Exception e) {
            System.out.println("Error purchasing ticket: " + e.getMessage());
        }
    }

    private void viewTickets(Scanner scanner) {
        try {
            System.out.print("Customer ID: ");
            Long userID = scanner.nextLong();
            // Assuming user ID is 1 for this example
            List<TicketEntity> tickets = ticketService.getTicketByCustomer(userID);

            if (tickets.isEmpty()) {
                System.out.println("No tickets found for the user.");
            } else {
                for (TicketEntity ticket : tickets) {
                    System.out.println("Ticket ID: " + ticket.getId());
                    System.out.println("Event Name: " + ticket.getEventName());
                    System.out.println("Quantity: " + ticket.getQuantity());
                    System.out.println("Total Price: " + ticket.getTotalPrice());
                    System.out.println("------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching tickets: " + e.getMessage());
        }
    }

    private void deleteTicket(Scanner scanner) {
        try {
            System.out.print("Enter ticket ID to delete: ");
            long ticketId = scanner.nextLong();

            boolean isDeleted = ticketService.deleteTicketById(ticketId);

            if (isDeleted) {
                System.out.println("Ticket deleted successfully.");
            } else {
                System.out.println("Ticket not found.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting ticket: " + e.getMessage());
        }
    }
}
