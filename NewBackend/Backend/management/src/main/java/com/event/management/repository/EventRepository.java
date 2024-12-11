package com.event.management.repository;

import com.event.management.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByVendorId(Long vendorId);

    @Query(value = "SELECT * FROM events", nativeQuery = true)
    List<EventEntity> findEventsByVendorId();

    @Query(value = "SELECT id, title, optional, description, organized_by, event_date, event_time, location, ticket_price, max_ticket_capacity, image, likes FROM events", nativeQuery = true)
    List<Object[]> findAllEventDetails();

    @Query(value = "SELECT id AS eventId, max_ticket_capacity AS ticketCapacity FROM events", nativeQuery = true)
    List<Object[]> findEventIdAndTicketCapacity();

}
