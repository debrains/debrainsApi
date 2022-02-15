package com.debrains.debrainsApi.repository;

import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
}
