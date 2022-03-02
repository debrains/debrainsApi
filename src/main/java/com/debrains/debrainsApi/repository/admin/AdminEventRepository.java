package com.debrains.debrainsApi.repository.admin;

import com.debrains.debrainsApi.entity.Event;
import com.debrains.debrainsApi.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminEventRepository extends JpaRepository<Event, Long> {
}
