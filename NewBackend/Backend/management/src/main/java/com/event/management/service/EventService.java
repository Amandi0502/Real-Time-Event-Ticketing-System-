package com.event.management.service;

import com.event.management.dto.EventDto;
import com.event.management.entity.EventEntity;
import com.event.management.entity.UserEntity;
import com.event.management.repository.EventRepository;
import com.event.management.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepo userRepository;

    public EventEntity createEvent(EventDto eventDto , Long userId) {

        // Find the vendor by ID
        UserEntity vendor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Create a new EventEntity and set its properties
        EventEntity event = new EventEntity();
        event.setVendor(vendor); // Associate the vendor with the event
        event.setTitle(eventDto.getTitle());
        event.setOptional(eventDto.getOptional());
        event.setDescription(eventDto.getDescription());
        event.setOrganizedBy(eventDto.getOrganizedBy());
        event.setEventDate(eventDto.getEventDate());
        event.setEventTime(eventDto.getEventTime());
        event.setLocation(eventDto.getLocation());
        event.setTicketPrice(eventDto.getTicketPrice());
        event.setMaxTicketCapacity(eventDto.getMaxTicketCapacity());
        event.setImage(eventDto.getImage());
        event.setLikes(eventDto.getLikes());

        // Save and return the new event
        return eventRepository.save(event);
    }

    public List<EventEntity> getEventsByVendor(Long vendorId) {
        return eventRepository.findByVendorId(vendorId);
    }

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Object[]> findAllEventDetails() {
        return eventRepository.findAllEventDetails();
    }

    public EventEntity getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + eventId + " not found"));
    }
}
