package com.event.management.service;

import com.event.management.cli.EventCLI;
import com.event.management.entity.EventEntity;
import com.event.management.entity.TicketEntity;
import com.event.management.entity.UserEntity;
import com.event.management.dto.TicketPurchaseDto;
import com.event.management.repository.EventRepository;
import com.event.management.repository.TicketRepository;
import com.event.management.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final EventRepository eventRepository;
    private final UserRepo userRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private final Map<Long, Integer> ticketPool = new HashMap<>(); // Event ID -> Available Tickets
    private final ReentrantLock lock = new ReentrantLock();

    public void initializeTicketPool() {
        // Fetch all event data and initialize the pool
        List<EventEntity> events = eventRepository.findAll();
        events.forEach(event -> ticketPool.put(event.getId(), event.getMaxTicketCapacity()));
    }

    public boolean purchaseTicketNew(TicketPurchaseDto ticketDto , Long user_id) {
        lock.lock();
        try {
            // Check if event exists in the pool
            if (!ticketPool.containsKey(ticketDto.getEventId())) {
                throw new RuntimeException("Event not found in ticket pool");
            }

            UserEntity customer = userRepository.findById(user_id)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            // Check ticket availability
            int availableTickets = ticketPool.get(ticketDto.getEventId());
            if (availableTickets < ticketDto.getQuantity()) {
                System.out.println("Not enough tickets available for event " + ticketDto.getEventId());
                return false;
            }

            // Update the ticket pool
            ticketPool.put(ticketDto.getEventId(), availableTickets - ticketDto.getQuantity());

            // Update the event's max ticket capacity in the database
            EventEntity event = eventRepository.findById(ticketDto.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            event.setMaxTicketCapacity(availableTickets - ticketDto.getQuantity());
            eventRepository.save(event);

            TicketEntity ticket = new TicketEntity();
            ticket.setEvent(event);
            ticket.setCustomer(customer);
            ticket.setQuantity(ticketDto.getQuantity());
            ticket.setTotalPrice(ticketDto.getTotalPrice());
            ticket.setName(ticketDto.getName());
            ticket.setContactNo(ticketDto.getContactNo());
            ticket.setEmail(ticketDto.getEmail());
            ticket.setEventDate(ticketDto.getEventDate());
            ticket.setEventTime(ticketDto.getEventTime());
            ticket.setEventName(ticketDto.getEventName());
            ticketRepository.save(ticket);
            System.out.println("Successfully purchased " + ticketDto.getQuantity() + " tickets for event " + ticketDto.getEventId());
            return true;
        } finally {
            lock.unlock();
        }
    }
    public TicketEntity purchaseTicket(TicketPurchaseDto ticketDto , Long user_id) {

        System.out.println("running..........: ");
        // Find the event by ID
        EventEntity event = eventRepository.findById(ticketDto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        System.out.println("event: " + event);
        // Find the customer by ID
        UserEntity customer = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        System.out.println("customer: " + customer);
        // Check if there are enough tickets available
        if (event.getMaxTicketCapacity() < ticketDto.getQuantity()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // Reduce the number of available tickets
        event.setMaxTicketCapacity(event.getMaxTicketCapacity() - ticketDto.getQuantity());
        eventRepository.save(event);

        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setQuantity(ticketDto.getQuantity());
        ticket.setTotalPrice(ticketDto.getTotalPrice());
        ticket.setName(ticketDto.getName());
        ticket.setContactNo(ticketDto.getContactNo());
        ticket.setEmail(ticketDto.getEmail());
        ticket.setEventDate(ticketDto.getEventDate());
        ticket.setEventTime(ticketDto.getEventTime());
        ticket.setEventName(ticketDto.getEventName());

        return ticketRepository.save(ticket);
    }

    public List<TicketEntity> getTicketByCustomer(Long vendorId) {
        return ticketRepository.findByCustomerId(vendorId);
    }

    @Transactional
    public boolean deleteTicketById(Long ticketId) {
        System.out.println("running deleting... ");
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
            return true;
        }
        return false;
    }

}
