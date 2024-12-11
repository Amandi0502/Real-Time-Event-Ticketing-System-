package com.event.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TICKETS")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private UserEntity customer;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contactNo;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String eventDate;

    @Column(nullable = false)
    private String eventTime;

    @Column(nullable = false)
    private String eventName;
}
