package com.event.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private Long vendorId; // ID of the vendor
    private String title; // Name of the event
    private String optional; // Optional details about the event
    private String description; // Detailed description of the event
    private String organizedBy; // Organizer's name
    private String eventDate; // Date of the event
    private String eventTime; // Time of the event
    private String location; // Location of the event
    private double ticketPrice; // Price per ticket
    private int maxTicketCapacity; // Maximum number of tickets available
    private String image; // URL for the event's image
    private int likes; // Initial number of likes
}
