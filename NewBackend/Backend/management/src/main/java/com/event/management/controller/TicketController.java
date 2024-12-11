package com.event.management.controller;

import com.event.management.config.jwtConfig.JwtTokenUtils;
import com.event.management.dto.TicketPurchaseDto;
import com.event.management.entity.TicketEntity;
import com.event.management.repository.TicketRepository;
import com.event.management.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final JwtTokenUtils jwtTokenUtils;
    @Autowired
    private TicketService ticketService;

    public TicketController(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestBody TicketPurchaseDto ticketDto) {
        try {

            // Access the SecurityContext to get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + authentication);

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                // Extract user ID from JWT token
                Long userId = jwtTokenUtils.getUserId(jwt);
                if (userId != null) {
                    System.out.println("UserID: " + userId);
                    ticketService.purchaseTicket(ticketDto , userId);
                    return ResponseEntity.ok("Ticket purchase successfully");
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Failed to purchase ticket " + e.getMessage());
        }
    }

    @GetMapping("/getTicket")
    public ResponseEntity<?> getTicketById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth: " + authentication);

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Extract user ID from JWT token
            Long userId = jwtTokenUtils.getUserId(jwt); // Ensure jwtTokenUtils.getUserId(jwt) is implemented
            if (userId != null) {
                System.out.println("UserID: " + userId);

                // Fetch tickets using the user ID
                List<TicketEntity> tickets = ticketService.getTicketByCustomer(userId); // Adjust service method name
                return ResponseEntity.ok(tickets); // Return the tickets as a response
            } else {
                System.out.println("UserID is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID");
            }
        } else {
            System.out.println("Principal is not an instance of Jwt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @DeleteMapping("/deleteTicket/{ticket_Id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticket_Id") Long ticketId) {
        try {
            // Attempt to delete the ticket using the service layer
            System.out.println("Delete start .....");
            boolean isDeleted = ticketService.deleteTicketById(ticketId);

            if (isDeleted) {
                return ResponseEntity.ok("Ticket deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the ticket: " + e.getMessage());
        }
    }



}
