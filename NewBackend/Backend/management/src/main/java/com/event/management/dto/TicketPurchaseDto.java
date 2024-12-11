package com.event.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPurchaseDto {

    private Long eventId;
    private String name;
    private String contactNo;
    private String email;
    private int quantity;
    private double totalPrice;
    private String eventDate;
    private String eventTime;
    private String eventName;
}
