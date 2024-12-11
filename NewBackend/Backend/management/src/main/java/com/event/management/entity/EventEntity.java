package com.event.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonBackReference
    private UserEntity vendor; // Vendor creating the event

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "optional")
    private String optional;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "organized_by")
    private String organizedBy;

    @Column(nullable = false, name = "event_date")
    private String eventDate;

    @Column(nullable = false, name = "event_time")
    private String eventTime;

    @Column(nullable = false, name = "location")
    private String location;

    @Column(nullable = false, name = "ticket_price")
    private double ticketPrice;

    @Column(nullable = false, name = "max_ticket_capacity")
    private int maxTicketCapacity; // New field added

    @Column(name = "image", length = 1000)
    private String image;

    @Column(name = "likes")
    private int likes;
}
