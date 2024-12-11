package com.event.management.repository;

import com.event.management.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByCustomerId(Long customer_id);
}
