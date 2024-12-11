package com.event.management.controller;

import com.event.management.config.jwtConfig.JwtTokenUtils;
import com.event.management.dto.EventDto;
import com.event.management.entity.EventEntity;
import com.event.management.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/events")
@CrossOrigin("http://localhost:5173")
public class EventController {

    private final EventService eventService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody EventDto eventDto) {
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
                    // Pass the userId to your service method
                    // Call the service to add the appointment
                    eventService.createEvent(eventDto , userId);
                    return ResponseEntity.ok("Event created successfully");
                } else {
                    System.out.println("UserID is null");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
                }
            } else {
                System.out.println("Principal is not an instance of Jwt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
            }
        } catch (Exception e) {
            // Return an error response with a BAD_REQUEST status
            return ResponseEntity.status(500).body("Failed to create Event" + e.getMessage());
        }
    }


    @GetMapping("/vendor")
    public ResponseEntity<?> getEventsByVendor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth: " + authentication);

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userId = jwtTokenUtils.getUserId(jwt);
            if (userId != null) {
                System.out.println("UserID: " + userId);
                List<EventEntity> events = eventService.getEventsByVendor(userId);
                return ResponseEntity.ok(events);
            } else {
                System.out.println("UserID is null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user ID");
            }
        } else {
            System.out.println("Principal is not an instance of Jwt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList().toString());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        List<EventEntity> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable Long eventId) {
        EventEntity event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }
}
